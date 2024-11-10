package com.example.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserIdentityDTO {
    @Schema(
        description = "Id of user", example = "1"
    )
    @NotNull(message = "userId must not be null or empty.")
    private Integer userId;
}