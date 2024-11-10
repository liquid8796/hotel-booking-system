package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.HotelDTO;
import com.example.hotelbooking.dto.UpdateHotelDTO;

import java.util.List;

public interface HotelService {
    List<HotelDTO> getAllHotels();

    List<HotelDTO> getAvailableHotels(String dateFrom, String dateTo);

    HotelDTO getHotelById(Integer id);

    HotelDTO createHotel(HotelDTO dto);

    HotelDTO updateHotel(UpdateHotelDTO dto);

    HotelDTO deleteHotelById(Integer id);
}
