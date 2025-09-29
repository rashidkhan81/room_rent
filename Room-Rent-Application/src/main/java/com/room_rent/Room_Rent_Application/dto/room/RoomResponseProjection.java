package com.room_rent.Room_Rent_Application.dto.room;

import com.room_rent.Room_Rent_Application.dto.file.FileResponse;
import com.room_rent.Room_Rent_Application.enums.room.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseProjection {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String contact;
    private String address;
    private RoomType roomType;
    private List<FileResponse> images;
    private String createdDate;
    private String createdTime;

    public RoomResponseProjection(Long id, String title, String description,
                           BigDecimal price, String contact, String address,
                           RoomType roomType, List<FileResponse> images,
                           LocalDateTime uploadedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.contact = contact;
        this.address = address;
        this.roomType = roomType;
        this.images = images;

        if (uploadedAt != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
            this.createdDate = uploadedAt.toLocalDate().format(dateFormatter);
            this.createdTime = uploadedAt.format(timeFormatter);
        }
    }
}