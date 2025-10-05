package com.room_rent.Room_Rent_Application.repository.room;

import com.room_rent.Room_Rent_Application.model.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {

//    @Query("""
//    SELECT r
//    FROM Room r
//    WHERE
//        (:searchKeyword IS NULL OR :searchKeyword = ''
//        OR LOWER(r.title) LIKE LOWER(CONCAT('%', :searchKeyword, '%'))
//        OR LOWER(r.description) LIKE LOWER(CONCAT('%', :searchKeyword, '%'))
//        OR LOWER(r.address) LIKE LOWER(CONCAT('%', :searchKeyword, '%'))
//        OR LOWER(r.city) LIKE LOWER(CONCAT('%', :searchKeyword, '%'))
//        OR LOWER(CAST(r.roomType AS string)) LIKE LOWER(CONCAT('%', :searchKeyword, '%')))
//    ORDER BY r.uploadedAt DESC
//""")
//    Page<Room> searchRooms(@Param("searchKeyword") String searchKeyword, Pageable pageable);


// recommendation
//    @Query("SELECT r.room FROM ReviewRating r " +
//            "GROUP BY r.room " +
//            "ORDER BY AVG(r.rating) DESC")
//    List<RoomResponseProjection> findTop5ByOrderByAverageRatingDesc();

}




