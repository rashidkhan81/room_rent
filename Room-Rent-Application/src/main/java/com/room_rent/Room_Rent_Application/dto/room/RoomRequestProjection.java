package com.room_rent.Room_Rent_Application.dto.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.room_rent.Room_Rent_Application.enums.room.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class    RoomRequestProjection {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotBlank(message = "Contact is required")
    private String contact;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

}
