package com.example.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(
    name = "Hotel",
    description = "Schema to hold Hotel information"
)
@Getter @Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class HotelIdentityDTO {
    @Schema(
        description = "Id of hotel", example = "1"
    )
    @NotNull(message = "hotelId must not be null or empty.")
    private Integer hotelId;
}
