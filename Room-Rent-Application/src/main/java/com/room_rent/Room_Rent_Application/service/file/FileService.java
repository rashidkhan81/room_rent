package com.room_rent.Room_Rent_Application.service.file;

import com.room_rent.Room_Rent_Application.dto.file.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    // Upload a single file for a specific room
    FileResponse uploadFile(MultipartFile file, Long roomId) throws IOException;

    // Upload multiple files for a specific room
    List<FileResponse> uploadMultipleFiles(MultipartFile[] files, Long roomId) throws IOException;

    // Get all files for the current user
    List<FileResponse> getAllFiles();

    // Get a single file by ID
    FileResponse getFile(Long id);

    // Delete a file by ID
    void deleteFile(Long id);
}
