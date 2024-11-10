package com.example.hotelbooking.entity;

import com.example.hotelbooking.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservations")
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
public class Reservation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Integer reservationId;

    @Column(name = "check_in_date")
    private String checkInDate;

    @Column(name = "check_out_date")
    private String checkOutDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "num_of_guests")
    private int guests;

    @Column(name = "status", length = 50)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties("bookings")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    @JsonIgnoreProperties("bookings")
    private Hotel hotel;

    @Override
    public String toString() {
        return "Reservation{" +
            "reservationId=" + reservationId +
            ", checkInDate='" + checkInDate + '\'' +
            ", checkOutDate='" + checkOutDate + '\'' +
            ", totalPrice=" + totalPrice +
            ", guests=" + guests +
            ", status=" + status +
            ", user=" + user +
            ", hotel=" + hotel +
            '}';
    }
}
