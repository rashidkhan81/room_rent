package com.room_rent.Room_Rent_Application.model.room;

import com.room_rent.Room_Rent_Application.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;
    private String waterFacility;
    private String contactNo;
    private String location;
    private String booking;  // AVAILABLE / BOOKED
    private String shareType; // SINGLE / DOUBLE / SHARED

    @ElementCollection
    private List<String> mediaUrls; // store uploaded file paths

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;  // the Admin who created this room
}
