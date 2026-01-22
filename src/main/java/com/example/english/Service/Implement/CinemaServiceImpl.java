package com.example.english.Service.Implement;

import com.example.english.Dto.Request.CinemaRequest;
import com.example.english.Dto.Response.CinemaResponse;
import com.example.english.Dto.Response.CinemaSummaryResponse;
import com.example.english.Dto.Response.CinemaTypeResponse;
import com.example.english.Entity.Cinema;
import com.example.english.Entity.CinemaType;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.CinemaMapper;
import com.example.english.Repository.CinemaRepository;
import com.example.english.Repository.CinemaTypeRepository;
import com.example.english.Service.Interface.CinemaService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CinemaServiceImpl implements CinemaService {
    CinemaMapper cinemaMapper;
    CinemaTypeRepository cinemaTypeRepository;
    CinemaRepository cinemaRepository;
    @Override
    public CinemaResponse createCinema(CinemaRequest cinemaRequest) {
        if(cinemaRepository.existsByName(cinemaRequest.getName())){
            throw  new AppException(ErrorCode.CINEMA_EXISTED);
        }
        CinemaType cinemaType = cinemaTypeRepository.findById(cinemaRequest.getCinemaTypeId()).
                orElseThrow(() -> new AppException(ErrorCode.CINEMA_TYPE_NOT_EXISTED));
        Cinema cinema = cinemaMapper.toCinema(cinemaRequest);
        cinema.setCinemaType(cinemaType);
        try {
            Cinema saved = cinemaRepository.save(cinema);
            CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(saved);
//            cinemaResponse.setCinemaTypeName(cinemaType.getName());
            cinemaResponse.setStatus(Boolean.TRUE.equals(cinema.getStatus()) ? "Đang hoạt đông" : "Tạm ngừng hoạt động");
            return cinemaResponse;
        }
        catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public CinemaResponse updateCinema(Long id, CinemaRequest cinemaRequest) {
        Cinema cinemaEntity = cinemaRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CINEMA_NOT_EXISTED));
        CinemaType cinemaType = cinemaTypeRepository.findById(cinemaRequest.getCinemaTypeId()).
                orElseThrow(() -> new AppException(ErrorCode.CINEMA_TYPE_NOT_EXISTED));
        cinemaMapper.updateCinemaFromRequest(cinemaRequest, cinemaEntity);
        cinemaEntity.setCinemaType(cinemaType);
        try {
            Cinema saved = cinemaRepository.save(cinemaEntity);
            CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(saved);
//            cinemaResponse.setCinemaTypeName(cinemaType.getName());
            cinemaResponse.setStatus(Boolean.TRUE.equals(cinemaEntity.getStatus()) ? "Đang hoạt đông" : "Tạm ngừng hoạt động");
            return cinemaResponse;
        }catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.DATA_VIOLATION);
        }
    }

    @Override
    public List<CinemaSummaryResponse> getCinemas(String address) {
        List<Cinema> cinemaEntities = cinemaRepository.findByAddressAndStatusTrue(address).orElseThrow(
                () -> new AppException(ErrorCode.CINEMA_NOT_EXISTED)
        );
        return cinemaMapper.toCinemaSummaryResponses(cinemaEntities);
    }

    @Override
    public CinemaResponse getCinema(Long id) {
        Cinema cinema = cinemaRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.CINEMA_NOT_EXISTED)
        );
        CinemaResponse cinemaResponse = cinemaMapper.toCinemaResponse(cinema);
        cinemaResponse.setStatus(Boolean.TRUE.equals(cinema.getStatus()) ? "Đang hoạt đông" : "Tạm ngừng hoạt động");
        return cinemaResponse;
    }

    @Override
    public Page<CinemaResponse> getListCinemas(Integer pageNumber, Integer sizeNumber) {
        if(pageNumber == null || pageNumber < 0){
            pageNumber = 0;
        }
        if(sizeNumber == null || sizeNumber <0 ){
            sizeNumber = 10;
        }
        Pageable pageable = PageRequest.of(pageNumber, sizeNumber);
        Page<Cinema> cinemaPage = cinemaRepository.findAll(pageable);
        if(cinemaPage.isEmpty()){
            throw new AppException(ErrorCode.CINEMA_NOT_EXISTED);
        }
        return cinemaPage.map(cinemaMapper::toCinemaResponse);
    }

    @Override
    public void deleteCinema(Long id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.CINEMA_NOT_EXISTED));
        cinema.setStatus(false);
        cinemaRepository.save(cinema);
    }

    @Override
    public List<CinemaTypeResponse> getCinemaTypes() {
        List<CinemaType> cinemas = cinemaTypeRepository.findAll();
        if(cinemas.isEmpty()){
            throw new AppException(ErrorCode.CINEMA_NOT_EXISTED);
        }
        return cinemaMapper.toCinemaTypeResponsesFromCinemaTypes(cinemas);
    }
}
