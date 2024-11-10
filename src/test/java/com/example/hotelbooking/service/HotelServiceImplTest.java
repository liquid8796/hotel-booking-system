package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.HotelDTO;
import com.example.hotelbooking.dto.UpdateHotelDTO;
import com.example.hotelbooking.entity.Hotel;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private UpdateHotelDTO updateHotelDTO;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() throws ParseException {
        hotel = new Hotel(1, "Luxury Hotel", "Luxury", "2024-11-01", "2024-12-31", "A five-star hotel", true, null);
        hotelDTO = new HotelDTO();
        hotelDTO.setHotelName("Luxury Hotel");
        hotelDTO.setAvailableFrom("2024-11-01");
        hotelDTO.setAvailableTo("2024-12-31");

        updateHotelDTO = new UpdateHotelDTO();
        updateHotelDTO.setHotelId(1);
        updateHotelDTO.setHotelName("Updated Hotel");
        updateHotelDTO.setAvailableFrom("2024-11-05");
        updateHotelDTO.setAvailableTo("2024-12-25");
        updateHotelDTO.setDescription("Updated Description");
        updateHotelDTO.setStatus(true);
    }

    @Test
    void testGetAllHotels() {
        when(hotelRepository.findAll()).thenReturn(List.of(hotel));
        when(objectMapper.convertValue(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        List<HotelDTO> hotels = hotelService.getAllHotels();
        assertEquals(1, hotels.size());
        assertEquals("Luxury Hotel", hotels.get(0).getHotelName());

        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testGetAvailableHotels_Success() {
        when(hotelRepository.findAllBetweenDates("2024-11-01", "2024-12-01"))
            .thenReturn(List.of(hotel));
        when(objectMapper.convertValue(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        List<HotelDTO> availableHotels = hotelService.getAvailableHotels("2024-11-01", "2024-12-01");
        assertEquals(1, availableHotels.size());
        assertEquals("Luxury Hotel", availableHotels.get(0).getHotelName());

        verify(hotelRepository, times(1)).findAllBetweenDates("2024-11-01", "2024-12-01");
    }

    @Test
    void testGetHotelById_Success() {
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));
        when(objectMapper.convertValue(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        HotelDTO result = hotelService.getHotelById(1);
        assertNotNull(result);
        assertEquals("Luxury Hotel", result.getHotelName());

        verify(hotelRepository, times(1)).findById(1);
    }

    @Test
    void testGetHotelById_NotFound() {
        when(hotelRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> hotelService.getHotelById(1));
    }

    @Test
    void testCreateHotel_Success() {
        when(objectMapper.convertValue(hotelDTO, Hotel.class)).thenReturn(hotel);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(objectMapper.convertValue(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        HotelDTO result = hotelService.createHotel(hotelDTO);
        assertNotNull(result);
        assertEquals("Luxury Hotel", result.getHotelName());

        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testUpdateHotel_Success() {
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(objectMapper.convertValue(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        HotelDTO result = hotelService.updateHotel(updateHotelDTO);
        assertNotNull(result);
        assertEquals("Luxury Hotel", result.getHotelName());

        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testUpdateHotel_NotFound() {
        when(hotelRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> hotelService.updateHotel(updateHotelDTO));
    }

    @Test
    void testDeleteHotelById_Success() {
        when(hotelRepository.findById(1)).thenReturn(Optional.of(hotel));
        when(hotelRepository.existsById(1)).thenReturn(false);
        when(objectMapper.convertValue(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        HotelDTO result = hotelService.deleteHotelById(1);
        assertNotNull(result);

        verify(hotelRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteHotelById_NotFound() {
        when(hotelRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> hotelService.deleteHotelById(1));
    }
}
