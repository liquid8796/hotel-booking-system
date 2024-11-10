package com.example.hotelbooking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Integer userId;

    private String name;

    private String email;

    private String phoneNumber;

    private List<ReservationDTO> reservations;
}