package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.*;
import com.example.hotelbooking.handler.model.Response;
import com.example.hotelbooking.service.ReservationService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ReservationController {
    private ReservationService reservationService;
    private MeterRegistry meterRegistry;

    @Autowired
    public ReservationController(ReservationService reservationService, MeterRegistry meterRegistry) {
        this.reservationService = reservationService;
        this.meterRegistry = meterRegistry;
    }

    /**
     * Endpoint to get all reservations.
     *
     * @return list of reservations
     */
    @GetMapping(value = "/reservations", produces = "application/json")
    public List<ReservationDTO> getAllReservations(){
        log.info("Get all reservations...");
        return reservationService.getAllReservations();
    }

    /**
     * Endpoint to get a specific reservation by id.
     *
     * @param id Integer
     * @return Reservation object
     */
    @GetMapping(value = "/reservation/{id}", produces = "application/json")
    public Response<ReservationDTO> getReservation(@PathVariable Integer id){
        log.info("Get a specified reservation with id = {}", id);
        ReservationDTO reservation = reservationService.getReservationById(id);

        return new Response<>(HttpStatus.OK.value(), "success", reservation);
    }

    /**
     * Search reservation between 2 dates of check in date
     *
     * @param dto SearchByDatesDTO
     * @return list reservations
     */
    @PostMapping(value = "/reservation/search-by-check-in-date")
    public List<ReservationDTO> getReservationBetweenCheckInDate(@Valid @RequestBody SearchByDatesDTO dto){
        return reservationService.findReservationBetweenCheckInDate(dto);
    }

    /**
     * Search reservation by hotel
     *
     * @param dto SearchByDatesDTO
     * @return list reservations
     */
    @PostMapping(value = "/reservation/search-by-hotel")
    public List<ReservationDTO> getReservationBetweenCheckInDate(@Valid @RequestBody SearchByHotelDTO dto){
        return reservationService.findReservationByHotelId(dto);
    }

    /**
     * Endpoint to make new reservation
     *
     * @param reservationDto NewReservationDTO
     * @return reservation
     */
    @PostMapping(value = "/reservation", produces = "application/json")
    public Response<ReservationDTO> createReservation(@Valid @RequestBody NewReservationDTO reservationDto){
        Counter counter = Counter.builder("api_create_reservation")
            .tag("reservations_counter", reservationDto.getHotel().getHotelId().toString())
            .description("a number of requests to /api/reservation endpoint")
            .register(meterRegistry);
        counter.increment();
        log.info("Booking a new reservation...");

        ReservationDTO reservation = reservationService.createReservation(reservationDto);

        return new Response<>(HttpStatus.CREATED.value(), "success", reservation);
    }

    /**
     * Endpoint to update a reservation
     *
     * @param dto UpdateReservationDTO
     * @return updated reservation
     */
    @PatchMapping(value = "/reservation", produces = "application/json")
    public Response<ReservationDTO> updateReservation(@Valid @RequestBody UpdateReservationDTO dto){
        log.info("Update reservation...");
        ReservationDTO reservation = reservationService.updateReservation(dto);

        return new Response<>(HttpStatus.OK.value(), "success", reservation);
    }

    /**
     * Endpoint to cancel reservation.
     *
     * @param id Integer
     * @return cancelled reservation
     */
    @PatchMapping(value = "/reservation/{id}/cancel", produces = "application/json")
    public Response<ReservationDTO> cancelReservation(@PathVariable Integer id){
        log.info("Cancel a reservation...");
        ReservationDTO reservation = reservationService.deleteReservationById(id);
        String message = !Objects.isNull(reservation) ? "success" : "fail";

        return new Response<>(HttpStatus.OK.value(), message, reservation);
    }
}
