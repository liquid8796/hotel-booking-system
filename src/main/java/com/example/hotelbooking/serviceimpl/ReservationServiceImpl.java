package com.example.hotelbooking.serviceimpl;

import com.example.hotelbooking.constants.ErrorConstant;
import com.example.hotelbooking.dto.*;
import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.entity.Reservation;
import com.example.hotelbooking.enums.ReservationStatus;
import com.example.hotelbooking.handler.exception.InvalidRequestException;
import com.example.hotelbooking.handler.exception.ResourceNotFoundException;
import com.example.hotelbooking.repository.HotelRepository;
import com.example.hotelbooking.repository.ReservationRepository;
import com.example.hotelbooking.repository.UserRepository;
import com.example.hotelbooking.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;

    private HotelRepository hotelRepository;

    private UserRepository userRepository;

    private ObjectMapper objectMapper;

    private final MeterRegistry meterRegistry;

    public ReservationServiceImpl(MeterRegistry meterRegistry, ObjectMapper objectMapper, UserRepository userRepository, HotelRepository hotelRepository, ReservationRepository reservationRepository) {
        this.meterRegistry = meterRegistry;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
        this.reservationRepository = reservationRepository;

        Gauge.builder("reservations_count", reservationRepository::count)
            .description("A current number of reservations in the system")
            .register(meterRegistry);
    }

    /**
     * Find reservation between 2 dates
     *
     * @param dto SearchByDatesDTO
     * @return list reservations
     */
    @Override
    public List<ReservationDTO> findReservationBetweenCheckInDate(SearchByDatesDTO dto) {
        Tag userTag = Tag.of("userId", String.valueOf(dto.getUserId()));
        Timer.Sample timer = Timer.start(meterRegistry);

        List<Reservation> reservations = reservationRepository.findReservationBetweenCheckInDate(dto.getDateFrom(), dto.getDateTo(), dto.getUserId());

        timer.stop(Timer.builder("service_reservations_find")
            .description("reservations searching timer")
            .tags(List.of(userTag))
            .register(meterRegistry));

        return reservations.stream()
            .map(reservation -> objectMapper.convertValue(reservation, ReservationDTO.class))
            .toList();
    }

    @Override
    public List<ReservationDTO> findReservationByHotelId(SearchByHotelDTO dto) {
        Tag userTag = Tag.of("userId", String.valueOf(dto.getUserId()));
        Timer.Sample timer = Timer.start(meterRegistry);

        List<Reservation> reservations = reservationRepository.findReservationByHotel_HotelIdAndUser_UserId(dto.getHotelId(), dto.getUserId());

        timer.stop(Timer.builder("service_reservations_find")
            .description("reservations searching timer")
            .tags(List.of(userTag))
            .register(meterRegistry));

        return reservations.stream()
            .map(reservation -> objectMapper.convertValue(reservation, ReservationDTO.class))
            .toList();
    }

    /**
     * Get a specific reservation by id
     *
     * @param id Integer
     * @return reservation
     */
    @Override
    public ReservationDTO getReservationById(Integer id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));

        return objectMapper.convertValue(reservation, ReservationDTO.class);
    }

    /**
     * Returns all reservations
     *
     * @return list reservations
     */
    @Override
    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
            .map(reservation -> objectMapper.convertValue(reservation, ReservationDTO.class))
            .toList();
    }

    /**
     * Make a new reservation
     *
     * @param newReservationDTO ReservationDTO
     * @return created reservation
     */
    @Transactional
    @Override
    public ReservationDTO createReservation(NewReservationDTO newReservationDTO) {
        ReservationDTO checkDto = objectMapper.convertValue(newReservationDTO, ReservationDTO.class);

        validateDates(checkDto);

        Reservation reservation = objectMapper.convertValue(checkDto, Reservation.class);
        reservation.setStatus(ReservationStatus.PENDING);
        Reservation savedReservation = reservationRepository.save(reservation);

        return objectMapper.convertValue(savedReservation, ReservationDTO.class);
    }

    /**
     * Update reservation
     *
     * @param updateReservationDTO UpdateReservationDTO
     * @return updated reservation
     */
    @Transactional
    @Override
    public ReservationDTO updateReservation(UpdateReservationDTO updateReservationDTO) {
        Reservation reservation = reservationRepository.findById(updateReservationDTO.getReservationId()).orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));

        ReservationDTO checkDto = objectMapper.convertValue(updateReservationDTO, ReservationDTO.class);

        validateDates(checkDto);

        reservation.setCheckInDate(checkDto.getCheckInDate());
        reservation.setCheckOutDate(checkDto.getCheckOutDate());
        reservation.setStatus(ReservationStatus.valueOf(checkDto.getStatus()));
        reservation.setTotalPrice(checkDto.getTotalPrice());
        reservation.setGuests(checkDto.getGuests());

        Reservation savedReservation = reservationRepository.save(reservation);

        return objectMapper.convertValue(savedReservation, ReservationDTO.class);
    }

    private void validateDates(ReservationDTO checkDto){
        Integer userId = checkDto.getUser().getUserId();
        Integer hotelId = checkDto.getHotel().getHotelId();

        userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel not found."));

        if(hasReservationOverlaps(checkDto)){
            throw new InvalidRequestException(ErrorConstant.INVALID_DATE_OVERLAP);
        }

        String availableFromDate = hotel.getAvailableFrom();
        String availableToDate = hotel.getAvailableTo();
        String checkInDate = checkDto.getCheckInDate();
        String checkOutDate = checkDto.getCheckOutDate();

        if (!(dateIsBefore(availableFromDate, checkInDate) && dateIsBefore(checkOutDate, availableToDate))) {
            throw new InvalidRequestException(ErrorConstant.INVALID_RESERVATION_DATES);
        }
    }

    /**
     * Checks to see if a reservation overlaps with an existing reservation in the database
     *
     * @param reservationDto ReservationDTO
     * @return boolean
     */
    private boolean hasReservationOverlaps(ReservationDTO reservationDto) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return reservationRepository.findAll().stream().filter(res -> !Objects.equals(res.getReservationId(), reservationDto.getReservationId())).anyMatch(res -> {
            if (res.getHotel().getHotelId() == reservationDto.getHotel().getHotelId()) {
                try {
                    int checkInBeforeDbCheckOut = sdf.parse(reservationDto.getCheckInDate()).compareTo(sdf.parse(res.getCheckOutDate()));
                    int checkOutBeforeDbCheckIn = sdf.parse(reservationDto.getCheckOutDate()).compareTo(sdf.parse(res.getCheckInDate()));
                    log.info("check in int {}", checkInBeforeDbCheckOut);
                    log.info("check out int {}", checkOutBeforeDbCheckIn);

                    if (checkInBeforeDbCheckOut == 0 || checkOutBeforeDbCheckIn == 0) {
                        return true;
                    } else {
                        return checkInBeforeDbCheckOut != checkOutBeforeDbCheckIn;
                    }
                } catch (ParseException e) {
                    throw new InvalidRequestException(ErrorConstant.PARSE_ERROR);
                }
            } else {
                return false;
            }
        });
    }

    /**
     * Check whether date1 is before date2
     *
     * @param date1 String
     * @param date2 String
     * @return boolean
     */
    private boolean dateIsBefore(String date1, String date2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return simpleDateFormat.parse(date1).before(simpleDateFormat.parse(date2));
        } catch (ParseException e) {
            throw new InvalidRequestException(ErrorConstant.PARSE_ERROR);
        }
    }

    /**
     * Deletes a user specified Reservation object from the database
     *
     * @param id Integer
     * @return reservation
     */
    @Override
    @Transactional
    public ReservationDTO deleteReservationById(Integer id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation not found."));

        LocalDateTime currentTime = LocalDateTime.now();
        int rowUpdated = reservationRepository.cancelReservationById(id, currentTime);

        ReservationDTO dto = objectMapper.convertValue(reservation, ReservationDTO.class);

        return rowUpdated > 0 ? dto : null;
    }
}
