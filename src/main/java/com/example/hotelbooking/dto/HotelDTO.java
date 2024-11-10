package com.example.hotelbooking.dto;

import com.example.hotelbooking.enums.HotelType;
import com.example.hotelbooking.handler.validation.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Schema(
    name = "Hotel",
    description = "Schema to hold Hotel information"
)
@Getter @Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class HotelDTO {
    private Integer hotelId;

    @Schema(
        description = "Name of the customer", example = "Phu My Hung"
    )
    @NotNull(message = "hotelName cannot be null or empty")
    @Size(min = 1, max = 100, message = "The length of the hotel name should be between 1 and 100")
    private String hotelName;

    @Schema(
        description = "Type of hotel", example = "Luxury"
    )
    @NotNull(message = "hotelType cannot be null or empty")
    @ValueOfEnum(enumClass = HotelType.class, message = "Type must be SUITE/ LUXURY/ DELUXE")
    private String hotelType;

    @Schema(
        description = "Description of hotel", example = "A Good hotel ever"
    )
    private String description;

    @Schema(
        description = "Available date from of hotel", example = "2024-11-01"
    )
    private String availableFrom;

    @Schema(
        description = "Available date to of hotel", example = "2024-12-31"
    )
    private String availableTo;

    @Schema(
        description = "Status of hotel", example = "true"
    )
    @NotNull(message = "status cannot be null or empty")
    private Boolean status;

    private List<ReservationDTO> reservations;
}
