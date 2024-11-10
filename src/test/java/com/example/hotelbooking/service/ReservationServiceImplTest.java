package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.*;
import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.entity.Reservation;
import com.example.hotelbooking.entity.User;
import com.example.hotelbooking.enums.ReservationStatus;
import com.example.hotelbooking.handler.exception.ResourceNotFoundException;
import com.example.hotelbooking.repository.HotelRepository;
import com.example.hotelbooking.repository.ReservationRepository;
import com.example.hotelbooking.repository.UserRepository;
import com.example.hotelbooking.serviceimpl.ReservationServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MeterRegistry meterRegistry;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private User mockUser;
    private UserDTO mockUserDto;
    private Hotel mockHotel;
    private HotelDTO mockHotelDto;
    private Reservation mockReservation;
    private ReservationDTO mockReservationDto;
    private NewReservationDTO mockNewReservationDTO;
    private UserIdentityDTO mockUserIdentityDTO;
    private HotelIdentityDTO mockHotelIdentityDTO;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUserId(1);
        mockUserDto = new UserDTO();
        mockUserDto.setUserId(1);

        mockUserIdentityDTO = new UserIdentityDTO();
        mockUserIdentityDTO.setUserId(1);

        mockHotelIdentityDTO = new HotelIdentityDTO();
        mockHotelIdentityDTO.setHotelId(1);

        mockHotel = new Hotel();
        mockHotel.setHotelId(1);
        mockHotel.setAvailableFrom("2024-11-01");
        mockHotel.setAvailableTo("2024-12-31");

        mockHotelDto = new HotelDTO();
        mockHotelDto.setHotelId(1);
        mockHotelDto.setAvailableFrom("2024-11-01");
        mockHotelDto.setAvailableTo("2024-12-31");

        mockReservation = new Reservation();
        mockReservation.setReservationId(1);
        mockReservation.setCheckInDate("2024-11-10");
        mockReservation.setCheckOutDate("2024-11-15");
        mockReservation.setUser(mockUser);
        mockReservation.setHotel(mockHotel);
        mockReservation.setStatus(ReservationStatus.PENDING);

        mockReservationDto = new ReservationDTO();
        mockReservationDto.setReservationId(1);
        mockReservationDto.setCheckInDate("2024-11-10");
        mockReservationDto.setCheckOutDate("2024-11-15");
        mockReservationDto.setUser(mockUserDto);
        mockReservationDto.setHotel(mockHotelDto);
        mockReservationDto.setStatus(ReservationStatus.PENDING.toString());

        mockNewReservationDTO = new NewReservationDTO();
        mockNewReservationDTO.setReservationId(1);
        mockNewReservationDTO.setCheckInDate("2024-11-10");
        mockNewReservationDTO.setCheckOutDate("2024-11-15");
        mockNewReservationDTO.setUser(mockUserIdentityDTO);
        mockNewReservationDTO.setHotel(mockHotelIdentityDTO);
    }

    @Test
    void testGetReservationById_Success() {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(mockReservation));
        when(objectMapper.convertValue(mockReservation, ReservationDTO.class)).thenReturn(new ReservationDTO());

        ReservationDTO result = reservationService.getReservationById(1);

        assertNotNull(result);
        verify(reservationRepository, times(1)).findById(1);
    }

    @Test
    void testGetReservationById_NotFound() {
        when(reservationRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.getReservationById(1));
        verify(reservationRepository, times(1)).findById(1);
    }

    @Test
    void testGetAllReservations() {
        when(reservationRepository.findAll()).thenReturn(List.of(mockReservation));
        when(objectMapper.convertValue(any(Reservation.class), eq(ReservationDTO.class))).thenReturn(new ReservationDTO());

        List<ReservationDTO> reservations = reservationService.getAllReservations();

        assertEquals(1, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testCancelReservationById_Success() {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(mockReservation));
        when(reservationRepository.cancelReservationById(eq(1), any(LocalDateTime.class))).thenReturn(1);

        String result = reservationService.cancelReservationById(1);

        assertEquals("success", result);

        verify(reservationRepository, times(1)).findById(1);
        verify(reservationRepository, times(1)).cancelReservationById(eq(1), any(LocalDateTime.class));
    }

    @Test
    void testCancelReservationById_NotFound() {
        when(reservationRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.cancelReservationById(1));

        verify(reservationRepository, times(1)).findById(1);
        verify(reservationRepository, never()).cancelReservationById(anyInt(), any(LocalDateTime.class));
    }

    @Test
    void testCancelReservationById_Fail() {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(mockReservation));
        when(reservationRepository.cancelReservationById(eq(1), any(LocalDateTime.class))).thenReturn(0);

        String result = reservationService.cancelReservationById(1);

        assertEquals("fail", result);

        verify(reservationRepository, times(1)).findById(1);
        verify(reservationRepository, times(1)).cancelReservationById(eq(1), any(LocalDateTime.class));
    }
}
