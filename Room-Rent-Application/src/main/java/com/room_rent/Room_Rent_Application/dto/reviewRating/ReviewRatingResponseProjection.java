package com.room_rent.Room_Rent_Application.dto.reviewRating;

import com.room_rent.Room_Rent_Application.dto.room.RoomResponseProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRatingResponseProjection {

    private Long id;
    private Float rating; // e.g., 1 to 5
    private String reviewText;
    //private Long roomId;
    private RoomResponseProjection room; // full room details

    private Long userId;

    private Double averageRating;


}
