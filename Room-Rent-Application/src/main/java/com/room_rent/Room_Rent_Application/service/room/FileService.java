package com.room_rent.Room_Rent_Application.service.room;

import com.room_rent.Room_Rent_Application.dto.room.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    FileResponse uploadFile(MultipartFile file) throws IOException;
    List<FileResponse> uploadMultipleFiles(MultipartFile[] files) throws IOException;
    List<FileResponse> getAllFiles();
    FileResponse getFile(Long id);
    FileResponse updateFile(Long id, MultipartFile newFile) throws IOException;
    void deleteFile(Long id);
}
