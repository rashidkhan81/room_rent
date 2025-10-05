package com.room_rent.Room_Rent_Application.model.room;

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

    @Column(name = "title")
    private String title;
    private String description;
    private BigDecimal price;
    private String address;
    private String contact;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private LocalDateTime uploadedAt;

    // ---- Relations ----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user", updatable = false)
    private User createdByUser;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomImage> images = new ArrayList<>();

}
