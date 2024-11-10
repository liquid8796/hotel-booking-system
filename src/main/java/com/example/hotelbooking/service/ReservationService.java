package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.*;

import java.util.List;

public interface ReservationService {
    List<ReservationDTO> getAllReservations();

    ReservationDTO getReservationById(Integer id);

    List<ReservationDTO> findReservationBetweenCheckInDate(SearchByDatesDTO dto);

    List<ReservationDTO> findReservationByHotelId(SearchByHotelDTO dto);

    ReservationDTO createReservation(NewReservationDTO dto);

    ReservationDTO updateReservation(UpdateReservationDTO dto);

    ReservationDTO deleteReservationById(Integer id);
}
