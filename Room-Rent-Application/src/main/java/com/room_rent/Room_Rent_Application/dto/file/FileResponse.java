package com.room_rent.Room_Rent_Application.dto.file;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
    private Long id;
    private String originalName;
    private String url;
    private String contentType;
    private long size;
    private LocalDateTime uploadedAt;
    private Long userId;
}