package com.example.hotelbooking.controller;

import com.example.hotelbooking.dto.HotelDTO;
import com.example.hotelbooking.dto.ReservationDTO;
import com.example.hotelbooking.dto.UpdateHotelDTO;
import com.example.hotelbooking.handler.model.Response;
import com.example.hotelbooking.service.HotelService;
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
public class HotelController {
    @Autowired
    private HotelService hotelService;

    /**
     * Endpoint to get all hotels
     *
     * @return list hotels
     */
    @GetMapping(value = "/hotels", produces = "application/json")
    public Response<List<HotelDTO>> getHotelList(){
        List<HotelDTO> hotels = hotelService.getAllHotels();
        log.info("Get all hotels from database, total: {}", hotels.size());

        return new Response<>(HttpStatus.OK.value(), "success", hotels);
    }

    /**
     * Endpoint to get a specified hotel
     *
     * @param id Integer
     * @return hotel
     */
    @GetMapping(value = "/hotel/{id}", produces = "application/json")
    public Response<HotelDTO> getHotel(@PathVariable Integer id) {
        log.info("Get hotel by id {}", id);
        HotelDTO hotel = hotelService.getHotelById(id);

        return new Response<>(HttpStatus.OK.value(), "success", hotel);
    }

    /**
     * End point to get list of Hotels available between user specified dates
     *
     * @param dateFrom String
     * @param dateTo String
     * @return list available hotels
     */
    @GetMapping(value = "/hotels/available", produces = "application/json")
    public List<HotelDTO> getAvailableHotels(@RequestParam("dateFrom") String dateFrom, @RequestParam("dateTo") String dateTo){
        log.info("Get all hotels available between dates from: {} to: {}", dateFrom, dateTo);

        return hotelService.getAvailableHotels(dateFrom, dateTo);
    }

    /**
     * Endpoint to create new hotel
     *
     * @param hotelDto HotelDTO
     * @return hotelDto
     */
    @PostMapping(value = "/hotel", produces = "application/json")
    public Response<HotelDTO> createHotel(@Valid @RequestBody HotelDTO hotelDto){
        log.info("Save new hotel with name: {}", hotelDto.getHotelName());
        HotelDTO hotel = hotelService.createHotel(hotelDto);

        return new Response<>(HttpStatus.CREATED.value(), "success", hotel);
    }

    /**
     * Endpoint to update a specified hotel.
     *
     * @param dto UpdateHotelDTO
     * @return updated hotel
     */
    @PatchMapping(value = "/hotel", produces = "application/json")
    public Response<HotelDTO> updateHotel(@RequestBody @Valid UpdateHotelDTO dto){
        log.info("Update hotel with id: {}", dto.getHotelId());
        HotelDTO hotel = hotelService.updateHotel(dto);

        return new Response<>(HttpStatus.OK.value(), "success", hotel);
    }

    /**
     * Endpoint to delete a specified hotel
     *
     * @param id Integer
     * @return Response
     */
    @DeleteMapping(value = "/hotel/{id}", produces = "application/json")
    public Response<HotelDTO> deleteHotel(@PathVariable Integer id){
        log.info("Delete hotel with id = {}", id);
        HotelDTO hotel = hotelService.deleteHotelById(id);
        String message = !Objects.isNull(hotel) ? "success" : "fail";

        return new Response<>(HttpStatus.OK.value(), message, hotel);
    }
}