package com.example.hotelbooking.dto;

import com.example.hotelbooking.enums.ReservationStatus;
import com.example.hotelbooking.handler.validation.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
    name = "Reservation",
    description = "Schema to hold Reservation information"
)
@Getter @Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UpdateReservationDTO {
    @Schema(
        description = "Id of reservation", example = "1"
    )
    @NotNull(message = "Id cannot be null or empty")
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
    @Positive(message = "totalPrice must be positive number")
    private Double totalPrice;

    @Schema(
        description = "Number of guests of reservation", example = "5"
    )
    @Min(value = 1, message = "guests must be at least 1 person")
    @Max(value = 8, message = "guests must be only at most 8 person")
    private Integer guests;

    @Schema(
        description = "Status of reservation", example = "PENDING"
    )
    @ValueOfEnum(enumClass = ReservationStatus.class, message = "Status must be CONFIRMED/ PENDING/ CANCELLED")
    private String status;

    @NotNull(message = "user cannot be null or empty")
    private UserIdentityDTO user;

    @NotNull(message = "hotel cannot be null or empty")
    private HotelIdentityDTO hotel;
}
