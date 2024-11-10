package com.example.hotelbooking.handler.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor
@Schema(
    name = "ErrorMessageResponse",
    description = "Schema to hold error response information"
)
public class ErrorMessageResponse {
    @Schema(
        description = "Error status code representing the error happened"
    )
    private int status;

    @Schema(
        description = "Error message representing the error happened"
    )
    private Object message;

    @Schema(
        description = "Time representing when the error happened"
    )
    private LocalDateTime timeStamp;
}
