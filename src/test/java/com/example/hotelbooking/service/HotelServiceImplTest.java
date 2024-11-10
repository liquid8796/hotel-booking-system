package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.HotelDTO;
import com.example.hotelbooking.dto.UpdateHotelDTO;
import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.handler.exception.InvalidRequestException;
import com.example.hotelbooking.handler.exception.ResourceNotFoundException;
import com.example.hotelbooking.repository.HotelRepository;
import com.example.hotelbooking.serviceimpl.HotelServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel hotel;
    private HotelDTO hotelDTO;

    @BeforeEach
    void setUp() {
        hotel = new Hotel(1, "Luxury Hotel", "LUXURY", "2024-11-01", "2024-12-31", "Best hotel in town", true, null);
        hotelDTO = new HotelDTO();
        hotelDTO.setHotelId(1);
        hotelDTO.setHotelName("Luxury Hotel");
        hotelDTO.setHotelType("LUXURY");
        hotelDTO.setAvailableFrom("2024-11-01");
        hotelDTO.setAvailableTo("2024-12-31");
        hotelDTO.setStatus(true);
    }

    @Test
    void testGetAllHotels() {
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(objectMapper.convertValue(any(Hotel.class), eq(HotelDTO.class))).thenReturn(hotelDTO);

        List<HotelDTO> result = hotelService.getAllHotels();

        assertEquals(1, result.size());
        assertEquals("Luxury Hotel", result.get(0).getHotelName());
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testGetAvailableHotels() {
        when(hotelRepository.findAllBetweenDates(anyString(), anyString())).thenReturn(List.of(hotel));
        when(objectMapper.convertValue(any(Hotel.class), eq(HotelDTO.class))).thenReturn(hotelDTO);

        List<HotelDTO> result = hotelService.getAvailableHotels("2024-11-01", "2024-12-31");

        assertEquals(1, result.size());
        assertEquals("Luxury Hotel", result.get(0).getHotelName());
        verify(hotelRepository, times(1)).findAllBetweenDates(anyString(), anyString());
    }

    @Test
    void testGetAvailableHotels_InvalidDates() {
        assertThrows(InvalidRequestException.class, () -> hotelService.getAvailableHotels("2024-12-31", "2024-11-01"));
    }

    @Test
    void testGetHotelById_Success() {
        when(hotelRepository.findById(anyInt())).thenReturn(Optional.of(hotel));
        when(objectMapper.convertValue(any(Hotel.class), eq(HotelDTO.class))).thenReturn(hotelDTO);

        HotelDTO result = hotelService.getHotelById(1);

        assertNotNull(result);
        assertEquals("Luxury Hotel", result.getHotelName());
        verify(hotelRepository, times(1)).findById(1);
    }

    @Test
    void testGetHotelById_NotFound() {
        when(hotelRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> hotelService.getHotelById(1));
    }

    @Test
    void testCreateHotel() {
        when(objectMapper.convertValue(any(HotelDTO.class), eq(Hotel.class))).thenReturn(hotel);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(objectMapper.convertValue(any(Hotel.class), eq(HotelDTO.class))).thenReturn(hotelDTO);

        HotelDTO result = hotelService.createHotel(hotelDTO);

        assertNotNull(result);
        assertEquals("Luxury Hotel", result.getHotelName());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testUpdateHotel() {
        UpdateHotelDTO updateHotelDTO = new UpdateHotelDTO();
        updateHotelDTO.setHotelId(1);
        updateHotelDTO.setHotelName("Updated Hotel");
        updateHotelDTO.setHotelType("SUITE");
        updateHotelDTO.setAvailableFrom("2024-11-01");
        updateHotelDTO.setAvailableTo("2024-12-31");
        updateHotelDTO.setStatus(true);

        when(hotelRepository.findById(anyInt())).thenReturn(Optional.of(hotel));
        when(objectMapper.convertValue(any(UpdateHotelDTO.class), eq(Hotel.class))).thenReturn(hotel);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(objectMapper.convertValue(any(Hotel.class), eq(HotelDTO.class))).thenReturn(hotelDTO);

        HotelDTO result = hotelService.updateHotel(updateHotelDTO);

        assertNotNull(result);
        assertEquals("Luxury Hotel", result.getHotelName());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testDeleteHotelById_Success() {
        when(hotelRepository.findById(anyInt())).thenReturn(Optional.of(hotel));

        doNothing().when(hotelRepository).deleteById(anyInt());

        when(hotelRepository.existsById(anyInt())).thenReturn(false);

        when(objectMapper.convertValue(any(Hotel.class), eq(HotelDTO.class))).thenReturn(hotelDTO);

        HotelDTO result = hotelService.deleteHotelById(1);

        assertNotNull(result, "The result should not be null");
        assertEquals("Luxury Hotel", result.getHotelName());
        verify(hotelRepository, times(1)).deleteById(anyInt());
        verify(hotelRepository, times(1)).existsById(anyInt());
    }

    @Test
    void testDeleteHotelById_NotFound() {
        when(hotelRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> hotelService.deleteHotelById(1));
    }
}
