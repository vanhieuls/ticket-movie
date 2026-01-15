package com.example.english.Service.Implement;

import com.example.english.Components.ShowTimeValidator;
import com.example.english.Dto.Request.ShowTimeRequest;
import com.example.english.Dto.Response.CinemaShowTimeResponse;
import com.example.english.Dto.Response.MovieShowTimeResponse;
import com.example.english.Dto.Response.ShowTimeDto;
import com.example.english.Dto.Response.ShowTimeResponse;
import com.example.english.Entity.Cinema;
import com.example.english.Entity.Movie;
import com.example.english.Entity.ScreenRoom;
import com.example.english.Entity.ShowTime;
import com.example.english.Exception.AppException;
import com.example.english.Exception.ErrorCode;
import com.example.english.Mapper.ShowTimeMapper;
import com.example.english.Repository.CinemaRepository;
import com.example.english.Repository.MovieRepository;
import com.example.english.Repository.ScreenRoomRepository;
import com.example.english.Repository.ShowTimeRepository;
import com.example.english.Service.Interface.ShowTimeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowTimeServiceImpl implements ShowTimeService {

    ShowTimeRepository showTimeRepository;
    ShowTimeMapper  showTimeMapper;
    MovieRepository movieRepository;
    ScreenRoomRepository screenRoomRepository;
    ShowTimeValidator showTimeValidator;
    CinemaRepository cinemaRepository;

    public Movie getMovie(Long id){
        return movieRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.MOVIE_NOT_FOUND));
    }

    public ScreenRoom getScreenRoom(Long id){
        return screenRoomRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.SCREEN_ROOM_NOT_EXISTED));
    }

    public ShowTime getShowTimeEntity(Long id){
       return showTimeRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.SHOW_TIME_NOT_FOUND));
    }

    @Override
    public ShowTimeResponse createShowTime(ShowTimeRequest showTimeRequest) {
        Movie movie = getMovie(showTimeRequest.getMovieId());
        ScreenRoom screenRoom = getScreenRoom(showTimeRequest.getScreenRoomId());

        long minutes = Duration.between(showTimeRequest.getStartTime(), showTimeRequest.getEndTime()).toMinutes();
        if(!showTimeRequest.getEndTime().isAfter(showTimeRequest.getStartTime()) || minutes > 3 * 60){
            throw new AppException(ErrorCode.INVALID_TIME_RANGE);
        }

        showTimeValidator.checkOverlap(showTimeRequest, null);

        ShowTime showTimeEntity = showTimeMapper.toShowTimeEntity(showTimeRequest);
        showTimeEntity.setStatus(true);
        showTimeEntity.setMovie(movie);
        showTimeEntity.setScreenRoom(screenRoom);
        return showTimeMapper.toShowTimeResponse(showTimeRepository.save(showTimeEntity));
    }

    @Override
    public ShowTimeResponse updateShowTime(ShowTimeRequest showTimeRequest, Long id) {
        ShowTime showTimeEntity = getShowTimeEntity(id);
        Movie movie = getMovie(showTimeRequest.getMovieId());
        ScreenRoom screenRoom = getScreenRoom(showTimeRequest.getScreenRoomId());
        long minutes = Duration.between(showTimeRequest.getStartTime(), showTimeRequest.getEndTime()).toMinutes();
        if(!showTimeRequest.getEndTime().isAfter(showTimeRequest.getStartTime()) || minutes > 3 * 60) {
            throw new AppException(ErrorCode.INVALID_TIME_RANGE);
        }
        showTimeValidator.checkOverlap(showTimeRequest, id);
        showTimeMapper.updateShowTimeFromRequest(showTimeRequest, showTimeEntity);
        showTimeEntity.setMovie(movie);
        showTimeEntity.setScreenRoom(screenRoom);
        return showTimeMapper.toShowTimeResponse(showTimeRepository.save(showTimeEntity));
    }

    @Override
    public ShowTimeResponse getShowTime(Long id) {
        return showTimeMapper.toShowTimeResponse(getShowTimeEntity(id));
    }

    @Override
    public List<ShowTimeDto> getShowTimeList(Long screenRoomId, LocalDate date) {
        List<ShowTime> showTimeEntityList = showTimeRepository.findByScreenRoom_IdAndShowDate(screenRoomId, date);
        return showTimeMapper.toShowTimeDtoList(showTimeEntityList);
    }

    @Override
    public List<MovieShowTimeResponse> getListShowTimeFilterCinema(Long cinemaId, LocalDate date) {
        if(!cinemaRepository.existsById(cinemaId)){
            throw new AppException(ErrorCode.CINEMA_NOT_EXISTED);
        }
        LocalTime currentTime = date.equals(LocalDate.now())
                ? LocalTime.now().minusMinutes(20)
                : LocalTime.MIDNIGHT;

        List<ShowTime> showTimes = showTimeRepository.findByScreenRoom_Cinema_IdAndShowDateAndStartTimeAfterAndStatusTrue(
                cinemaId,
                date,
                currentTime
        );
        Map<Movie , List<ShowTime>> grouped = showTimes.stream()
                .collect(Collectors.groupingBy(ShowTime::getMovie));
        List<MovieShowTimeResponse> movieShowTimeResponseList = new ArrayList<>();

        for(Map.Entry<Movie, List<ShowTime>>  entry : grouped.entrySet()) {
            Movie movieEntity = entry.getKey();
            List<ShowTime> showTimeEntityList =  entry.getValue();

            MovieShowTimeResponse movieDto = new MovieShowTimeResponse();
            movieDto.setMovieId(movieEntity.getId());
            movieDto.setMovieName(movieEntity.getName());
            movieDto.setPosterUrl(movieEntity.getPosterUrl());

            List<MovieShowTimeResponse.ShowTimeSummaryResponse> showTimeDtoList = showTimeEntityList.stream()
                    .map(st -> {
                        MovieShowTimeResponse.ShowTimeSummaryResponse showTimeDto =
                                new  MovieShowTimeResponse.ShowTimeSummaryResponse();
                        showTimeDto.setShowTimeId(st.getId());
                        showTimeDto.setStartTime(st.getStartTime());
                        showTimeDto.setEndTime(st.getEndTime());
                        return showTimeDto;
                    })
                    .toList();
            movieDto.setShowTimeSummaryResponseList(showTimeDtoList);
            movieShowTimeResponseList.add(movieDto);
        }
        return movieShowTimeResponseList;
    }

    @Override
    public List<CinemaShowTimeResponse> getListShowTimeFilterMovie(Long movieId, LocalDate date, String address) {
        if(!movieRepository.existsById(movieId)) {
            throw new AppException(ErrorCode.MOVIE_NOT_FOUND);
        }

        LocalTime currentTime = date.equals(LocalDate.now())
                ? LocalTime.now().minusMinutes(20)
                : LocalTime.MIDNIGHT;
        List<ShowTime> showTimeList = showTimeRepository.findByScreenRoom_Cinema_AddressAndMovie_IdAndShowDateAndStartTimeAfterAndStatusTrue(
                address,
                movieId,
                date,
                currentTime
        );
        Map<Cinema, List<ShowTime>> grouped = showTimeList.stream()
                .collect(Collectors.groupingBy(st -> st.getScreenRoom().getCinema()));

        List<CinemaShowTimeResponse> cinemaShowTimeResponseList =  new ArrayList<>();

        for(Map.Entry<Cinema , List<ShowTime>>  entry : grouped.entrySet()) {
            Cinema cinemaEntity = entry.getKey();
            List<ShowTime> showTimeEntityList =  entry.getValue();

            CinemaShowTimeResponse cinemaDto = new CinemaShowTimeResponse();
            cinemaDto.setCinemaId(cinemaEntity.getId());
            cinemaDto.setCinemaName(cinemaEntity.getName());

            List<CinemaShowTimeResponse.ShowTimeSummaryResponse> showTimeDtoList = showTimeEntityList.stream()
                    .map(st -> {
                        CinemaShowTimeResponse.ShowTimeSummaryResponse showTimeDto =
                                new  CinemaShowTimeResponse.ShowTimeSummaryResponse();
                        showTimeDto.setShowTimeId(st.getId());
                        showTimeDto.setStartTime(st.getStartTime());
                        showTimeDto.setEndTime(st.getEndTime());
                        return showTimeDto;
                    })
                    .toList();
            cinemaDto.setShowTimeSummaryResponseList(showTimeDtoList);
            cinemaShowTimeResponseList.add(cinemaDto);
        }
        return cinemaShowTimeResponseList;
    }

    @Override
    public List<ShowTimeDto> getShowTimeDto(String movieName, String cinemaName, LocalDate date) {
        List<ShowTime> showTimeEntityList = showTimeRepository.findByMovie_NameAndScreenRoom_Cinema_NameAndShowDateAndStatusTrue(
                movieName, cinemaName, date
        );
        return showTimeMapper.toShowTimeDtoList(showTimeEntityList);
    }

    @Override
    public void deleteShowTime(Long id) {
        ShowTime showTime = getShowTimeEntity(id);
        showTime.setStatus(false);
        showTimeRepository.save(showTime);
    }
}
