package com.room_rent.Room_Rent_Application.model.reviewRating;

import com.room_rent.Room_Rent_Application.common.AbstractEntity;
import com.room_rent.Room_Rent_Application.model.User;
import com.room_rent.Room_Rent_Application.model.room.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review_rating")
public class ReviewRating extends AbstractEntity implements Serializable {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float rating; // e.g., 1 to 5
    private String reviewText;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room; // which room is reviewed

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // who gave the review


}
