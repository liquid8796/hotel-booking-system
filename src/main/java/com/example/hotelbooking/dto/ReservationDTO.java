package com.example.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Schema(
    name = "Reservation",
    description = "Schema to hold Reservation information"
)
@Getter @Setter @ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ReservationDTO {
    private Integer reservationId;

    @Schema(
        description = "Check in date of reservation", example = "2024-11-01"
    )
    private String checkInDate;

    @Schema(
        description = "Check out date of reservation", example = "2024-11-03"
    )
    private String checkOutDate;

    @Schema(
        description = "Total price of reservation", example = "1500"
    )
    private Double totalPrice;

    @Schema(
        description = "Number of guests of reservation", example = "5"
    )
    @Min(value = 1, message = "guests must be at least 1 person")
    @Max(value = 8, message = "guests must be only at most 8 person")
    private int guests;

    @Schema(
        description = "Status of reservation", example = "PENDING"
    )
    private String status;

    private UserDTO user;

    private HotelDTO hotel;
}
