package com.example.hotelbooking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SearchByDatesDTO {
    @Schema(
        description = "Date search of reservation", example = "2024-11-03"
    )
    @NotNull(message = "dateFrom cannot be null or empty")
    private String dateFrom;

    @Schema(
        description = "Date search of reservation", example = "2024-11-03"
    )
    @NotNull(message = "dateTo cannot be null or empty")
    private String dateTo;

    @NotNull(message = "userId cannot be null or empty")
    private Integer userId;
}
