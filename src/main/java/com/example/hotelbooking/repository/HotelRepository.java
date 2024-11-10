package com.example.hotelbooking.repository;

import com.example.hotelbooking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    @Query(value = "SELECT * FROM hotels h  WHERE h.available_from >= :startDate AND h.available_to <= :endDate AND h.hotel_id NOT IN " +
        "(SELECT hotel_id FROM reservation WHERE (check_in_date >= :startDate OR check_out_date <= :endDate))", nativeQuery = true)
    List<Hotel> findAllBetweenDates(@Param("startDate") String startDate, @Param("endDate") String endDate);
}