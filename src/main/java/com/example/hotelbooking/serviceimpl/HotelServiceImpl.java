package com.example.hotelbooking.serviceimpl;

import com.example.hotelbooking.constants.ErrorConstant;
import com.example.hotelbooking.dto.HotelDTO;
import com.example.hotelbooking.dto.UpdateHotelDTO;
import com.example.hotelbooking.entity.Hotel;
import com.example.hotelbooking.handler.exception.InvalidRequestException;
import com.example.hotelbooking.handler.exception.ResourceNotFoundException;
import com.example.hotelbooking.repository.HotelRepository;
import com.example.hotelbooking.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@Service
public class HotelServiceImpl implements HotelService {
    private static final String VALID_DATE_FORMAT = "yyyy-MM-dd";
    private static final DateFormat simpleDateFormat = new SimpleDateFormat(VALID_DATE_FORMAT);

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Get all hotels
     *
     * @return list of hotel
     */
    @Override
    public List<HotelDTO> getAllHotels() {

        return hotelRepository.findAll().stream()
            .map(hotel -> objectMapper.convertValue(hotel, HotelDTO.class))
            .toList();
    }

    /**
     * Get all hotel that are available in between a specified dates
     *
     * @param dateFrom String
     * @param dateTo String
     * @return list available hotels
     */
    @Override
    public List<HotelDTO> getAvailableHotels(String dateFrom, String dateTo) {
        validateDates(dateFrom, dateTo);

        List<Hotel> allBetweenDates = hotelRepository.findAllBetweenDates(dateFrom, dateTo);

        return allBetweenDates.stream()
            .map(hotel -> objectMapper.convertValue(hotel, HotelDTO.class))
            .toList();
    }

    /**
     * Get hotel by id
     * @param id Integer
     * @return hotel
     */
    @Override
    public HotelDTO getHotelById(Integer id) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found."));

        return objectMapper.convertValue(hotel, HotelDTO.class);
    }

    /**
     * Create new hotel
     *
     * @param dto HotelDTO
     * @return created hotel
     */
    @Override
    @Transactional
    public HotelDTO createHotel(HotelDTO dto) {
        validateDates(dto.getAvailableFrom(), dto.getAvailableTo());

        Hotel savedHotel = hotelRepository.save(objectMapper.convertValue(dto, Hotel.class));

        return objectMapper.convertValue(savedHotel, HotelDTO.class);
    }

    /**
     * Updates an existing Hotel
     *
     * @param dto HotelDTO
     * @return updated hotel
     */
    @Override
    @Transactional
    public HotelDTO updateHotel(UpdateHotelDTO dto) {
        validateDates(dto.getAvailableFrom(), dto.getAvailableTo());

        hotelRepository.findById(dto.getHotelId()).orElseThrow(() -> new ResourceNotFoundException("Hotel not found."));
        Hotel hotel = hotelRepository.save(objectMapper.convertValue(dto, Hotel.class));

        return objectMapper.convertValue(hotel, HotelDTO.class);
    }

    /**
     * Delete hotel by id
     *
     * @param id Integer
     * @return deleted hotel
     */
    @Override
    @Transactional
    public HotelDTO deleteHotelById(Integer id) {
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found."));
        hotelRepository.deleteById(id);

        boolean existsById = hotelRepository.existsById(id);

        HotelDTO hotelDTO = objectMapper.convertValue(hotel, HotelDTO.class);

        return !existsById ? hotelDTO : null;
    }

    /**
     * Validator for date format yyyy-MM-dd
     *
     * @param date String
     * @return
     */
    private boolean validateDateFormat(String date) {
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(date);
        } catch (ParseException e) {
            log.error("Invalid date format: '{}', please input dates in 'yyyy-MM-dd' format.", date);
            throw new InvalidRequestException(ErrorConstant.INVALID_DATE);
        }

        return true;
    }

    /**
     * Validator for 'startDate' and 'endDate'
     *
     * @param startDate String
     * @param endDate String
     */
    private void validateDates(String startDate, String endDate) {
        if (startDate == null || endDate == null) {
            throw new InvalidRequestException(ErrorConstant.INVALID_DATE_NULL_VALUES);
        }

        if (validateDateFormat(startDate) && validateDateFormat(endDate)) {
            try {
                if (simpleDateFormat.parse(startDate).after(simpleDateFormat.parse(endDate))) {
                    log.error("Start date: '{}' mus be before end date: '{}'.", startDate, endDate);
                    throw new InvalidRequestException(ErrorConstant.INVALID_DATE_ORDER);
                }
            } catch (ParseException e) {
                log.debug("Invalid date comparison.");
            }
        } else {
            log.error("Invalid date: Please input dates in 'yyyy-MM-dd' format.");
            throw new InvalidRequestException(ErrorConstant.INVALID_DATE);
        }
    }
}
