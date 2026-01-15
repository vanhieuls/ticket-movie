package com.example.english.Service.Implement;

import com.example.english.Dto.Request.SeatRequest;
import com.example.english.Dto.Response.SeatResponse;
import com.example.english.Dto.Response.SeatShowTime;
import com.example.english.Dto.Response.SeatShowTimeResponse;
import com.example.english.Dto.Response.SeatSummaryRepo;
import com.example.english.Entity.*;
import com.example.english.Enum.SeatStatus;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.SeatMapper;
import com.example.english.Repository.*;
import com.example.english.Service.Interface.SeatService;
import com.example.english.Service.Interface.TicketPriceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    SeatRepository seatRepository;
    SeatMapper seatMapper;
    TicketRepository ticketRepository;
    ShowTimeRepository showTimeRepository;
    TicketPriceService ticketPriceService;
    ScreenRoomRepository screenRoomRepository;
    SeatTypeRepository seatTypeRepository;
    RedisService redisService;
    @Override
    public List<SeatResponse> createSeats(SeatRequest seatRequest) {
        Long[][] matrix = seatRequest.getSeatTypeId();

        // Lấy phòng
        ScreenRoom screenRoom = screenRoomRepository.findById(seatRequest.getScreenRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.SCREEN_ROOM_NOT_EXISTED));

        // Gom tất cả seatTypeId sẽ dùng để bulk fetch
        Set<Long> seatTypeIds = new HashSet<>();
        for (int i = 0; i < seatRequest.getRows(); i++) {
            for (int j = 0; j < seatRequest.getColumns(); j++) {
                Long id = matrix[i][j];
                if (id != null && id != 0L)
                    seatTypeIds.add(id);
            }
        }

        // Nếu không có ghế hợp lệ, trả rỗng
        if (seatTypeIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Bulk fetch seat types
        Map<Long, SeatType> seatTypeMap = seatTypeRepository.findAllById(seatTypeIds)
                .stream()
                .collect(Collectors.toMap(SeatType::getId, seatType -> seatType));

        //Kiểm tra seat type nào không tồn tại
        if (seatTypeMap.size() != seatTypeIds.size()) {
            // tìm id thiếu
            Set<Long> missing = new HashSet<>(seatTypeIds);
            missing.removeAll(seatTypeMap.keySet());
            throw new AppException(ErrorCode.SEAT_NOT_FOUND);
        }

        // Tạo ghế
        List<Seat> toSave = new ArrayList<>();
        for (int i = 0; i < seatRequest.getRows(); i++) {
            for (int j = 0; j < seatRequest.getColumns(); j++) {
                Long seatTypeId = matrix[i][j];
                if (seatTypeId == null || seatTypeId == 0L) continue;
                SeatType seatType = seatTypeMap.get(seatTypeId);
                Seat seat = Seat.builder()
                        .seatType(seatType)
                        .seatCode(genSeatCode(i, j))
                        .row(i)
                        .column(j)
                        .isActive(true)
                        .screenRoom(screenRoom)
                        .build();
                toSave.add(seat);
            }
        }
        return seatMapper.toSeatResponseList(seatRepository.saveAll(toSave));
    }
    private String genSeatCode(int row, int column) {
        char code= (char) ('A' + row);
        return code + String.valueOf(column + 1);
    }
    @Override
    public SeatShowTime getListSeat(Long showTimeId) {
        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new AppException(ErrorCode.SHOWTIME_NOT_FOUND));
        Long screenRoomId = showTime.getScreenRoom().getId();
        List<Seat> seatEntityList = seatRepository.findByScreenRoom_Id(screenRoomId);
        List<Seat> seatListBooked = ticketRepository.findByShowTime_Id(showTimeId).stream()
                .map(Ticket::getSeat)
                .toList();

        //Lấy ghế đã BOOKED theo showTime -> convert sang Set để contains O(1)
        Set<Long> bookedSeatIds = seatListBooked.stream()
                .map(Seat::getId)
                .collect(Collectors.toSet());

        List<SeatShowTimeResponse> seatList = seatEntityList.stream()
                .map(seat -> {
                    SeatShowTimeResponse seatResponse = new SeatShowTimeResponse();
                    TicketPrice ticketPrice = ticketPriceService.getSeatTicketPrice(
                            showTime.getScreenRoom().getCinema().getCinemaType().getId(),
                            showTime.getScreenRoom().getScreenRoomType().getId(),
                            seat.getSeatType().getId(),
                            showTime.getShowDate(),
                            showTime.getStartTime()
                    );
                    seatResponse.setPrice(ticketPrice.getPrice());
                    seatResponse.setSeatCode(seat.getSeatCode());
                    seatResponse.setSeatTypeName(seat.getSeatType().getName());
                    if (bookedSeatIds.contains(seat.getId())) {
                        seatResponse.setStatus(SeatStatus.BOOKED);
                    } else if (redisService.isSeatHoldingList( showTimeId, seat.getId())) {
                        seatResponse.setStatus(SeatStatus.HOLDING);
                    } else {
                        seatResponse.setStatus(SeatStatus.AVAILABLE);
                    }
                    seatResponse = seatMapper.toSeatShowTimeResponse(seat);
                    return seatResponse;
                }).toList();
        return SeatShowTime.builder()
                .showTimeId(showTimeId)
                .CinemaName(showTime.getScreenRoom().getCinema().getName())
                .MovieName(showTime.getMovie().getName())
                .sumSeats(seatRepository.countByScreenRoom_Id(screenRoomId))
                .seatList(seatList)
                .build();
    }

    @Override
    public List<SeatSummaryRepo> getSeatListByScreenRoom(long showRoomId) {
        List<Seat> seatList = seatRepository.findByScreenRoom_Id(showRoomId);
        return seatMapper.toSeatSummaryRepoList(seatList);
    }

    @Override
    public void updateActiveSeat(Long id) {
        Seat seat = seatRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.SEAT_NOT_FOUND));
        seat.setActive(!seat.isActive());
        seatRepository.save(seat);
    }
}
