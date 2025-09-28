package com.room_rent.Room_Rent_Application.repository.room;

import com.room_rent.Room_Rent_Application.model.files.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<RoomImage, Long> {
   // List<Room> findByCreatedBy(User user);

//    @Query("SELECT r FROM Room r WHERE " +
//            "(:location IS NULL OR r.location = :location) AND " +
//            "(:minPrice IS NULL OR r.price >= :minPrice) AND " +
//            "(:maxPrice IS NULL OR r.price <= :maxPrice)")
//    List<Room> filterRooms(@Param("location") String location,
//                           @Param("minPrice") BigDecimal minPrice,
//                           @Param("maxPrice") BigDecimal maxPrice);

    List<RoomImage> findByCreatedBy(Long createdBy);
}
