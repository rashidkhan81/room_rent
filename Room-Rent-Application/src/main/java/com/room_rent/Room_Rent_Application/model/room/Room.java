package com.room_rent.Room_Rent_Application.model.room;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.room_rent.Room_Rent_Application.common.AbstractEntity;
import com.room_rent.Room_Rent_Application.enums.room.RoomType;
import com.room_rent.Room_Rent_Application.model.User;
import com.room_rent.Room_Rent_Application.model.files.RoomImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---- Room Basic Info ----
    @Column(name = "title")
    private String title;            // short title e.g. "2BHK Apartment in Baneshwor"
    private String description;      // detailed description
    private BigDecimal price;        // monthly rent
    private String address;          // full address
    private String contact;
    private String city;             // city
    private String district;         // district or state


    // ---- Room Details ----
    private int numberOfRooms;       // e.g. 2 bedrooms, 1 kitchen
    private int floor;               // which floor
    private boolean furnished;       // furnished or not
    private double area;             // size in sq. ft/m²

    @Enumerated(EnumType.STRING)
    private RoomType roomType;       // SINGLE, DOUBLE, APARTMENT, FLAT, etc.

    // ---- Amenities ----
    private boolean parkingAvailable;
    private boolean wifiAvailable;
    private boolean waterSupply;
    private boolean electricityBackup;

    // ---- Owner / Contact ----
    private String ownerName;
    private String contactNumber;
    private String email;

    // ---- Media / Image Info ----
    private String originalName;
    private String uniqueName;
    private String contentType;
    private long size;
    private String url;

    private LocalDateTime uploadedAt;

    // ---- Relations ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user", updatable = false)
    private User createdByUser;


    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomImage> images = new ArrayList<>();

}
