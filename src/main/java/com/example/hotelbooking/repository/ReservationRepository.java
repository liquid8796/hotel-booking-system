package com.example.hotelbooking.repository;

import com.example.hotelbooking.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query(value = "SELECT * FROM reservations res WHERE res.check_in_date BETWEEN :dateFrom AND :dateTo AND user_id = :userId", nativeQuery = true)
    List<Reservation> findReservationBetweenCheckInDate(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo, @Param("userId") int userId);

    List<Reservation> findReservationByHotel_HotelIdAndUser_UserId(Integer hotelId, Integer userId);

    @Modifying
    @Query(value = "UPDATE reservations r SET r.status = 'CANCELLED', r.updated_at = :updatedAt WHERE r.reservation_id = :id", nativeQuery = true)
    int cancelReservationById(@Param("id") Integer id, @Param("updatedAt") LocalDateTime updatedAt);
}