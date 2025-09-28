package com.room_rent.Room_Rent_Application.model.room;

import com.room_rent.Room_Rent_Application.common.AbstractEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "rooms")
@FilterDef(name = "createdByFilter", parameters = @ParamDef(name = "userId", type = Long.class))
@Filter(name = "createdByFilter", condition = "created_by = :userId")
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
