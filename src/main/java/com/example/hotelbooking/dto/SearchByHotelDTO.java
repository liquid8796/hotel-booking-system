package com.example.hotelbooking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchByHotelDTO {
    @Schema(
        description = "Id of hotel", example = "1"
    )
    @NotNull(message = "hotelId cannot be null or empty")
    private Integer hotelId;

    @Schema(
        description = "Id of user", example = "2"
    )
    @NotNull(message = "userId cannot be null or empty")
    private Integer userId;
}
