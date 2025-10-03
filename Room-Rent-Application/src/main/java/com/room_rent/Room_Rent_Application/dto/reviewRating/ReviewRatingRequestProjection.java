package com.room_rent.Room_Rent_Application.dto.reviewRating;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRatingRequestProjection {

    private Float rating; // e.g., 1 to 5
    private String reviewText;
   // private Integer reviewerId;
    private Long roomId;
}
