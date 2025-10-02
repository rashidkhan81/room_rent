package com.room_rent.Room_Rent_Application.model.booking;


import com.room_rent.Room_Rent_Application.common.AbstractEntity;
import com.room_rent.Room_Rent_Application.enums.room.booking.BookingStatus;
import com.room_rent.Room_Rent_Application.model.User;
import com.room_rent.Room_Rent_Application.model.room.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
public class Booking extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who booked
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Which room
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;


    private LocalDateTime localDateTime = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    // as per my undestand the user going to book the room then i need to fetch that room by id and also
    // need to figure out who has posted the that room and send the notification email to admin that
    // some has requested for the room

    // once the admin approved the booking request i need to send the notificaton to the user in email that you room has been approved  congrats

}
