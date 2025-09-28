package com.room_rent.Room_Rent_Application.model.room;

import com.room_rent.Room_Rent_Application.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "rooms")
public class Room extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalName;
    private String uniqueName;
    private String contentType;
    private long size;
    private String url;

     private LocalDateTime uploadedAt;


}
