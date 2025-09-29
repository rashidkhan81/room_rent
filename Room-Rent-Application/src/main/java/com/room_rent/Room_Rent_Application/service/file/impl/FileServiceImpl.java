package com.room_rent.Room_Rent_Application.service.file.impl;

import com.room_rent.Room_Rent_Application.common.ForApiRestrication.SecurityUtils;
import com.room_rent.Room_Rent_Application.config.fileConfig.FileStorageConfig;
import com.room_rent.Room_Rent_Application.dto.file.FileResponse;
import com.room_rent.Room_Rent_Application.exception.FileStorageException.FileStorageException;
import com.room_rent.Room_Rent_Application.model.files.RoomImage;
import com.room_rent.Room_Rent_Application.model.room.Room;
import com.room_rent.Room_Rent_Application.repository.file.FileRepository;
import com.room_rent.Room_Rent_Application.repository.room.RoomRepository;
import com.room_rent.Room_Rent_Application.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileStorageConfig fileStorageConfig;
    private final RoomRepository roomRepository;

    private Path getUploadPath() {
        return Path.of(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
    }

    // Single file upload with Room association
    @Override
    public FileResponse uploadFile(MultipartFile file, Long roomId) throws IOException {
        if (file.isEmpty()) {
            throw new FileStorageException("Cannot upload empty file.");
        }

        // Generate unique file name
        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetLocation = getUploadPath().resolve(uniqueName);

        // Ensure directory exists
        Files.createDirectories(targetLocation.getParent());

        // Copy file to storage
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Fetch Room from DB
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new FileStorageException("Room not found with id " + roomId));

        // Save metadata in DB
        RoomImage roomImageFile = RoomImage.builder()
                .originalName(file.getOriginalFilename())
                .uniqueName(uniqueName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .url("/files/" + uniqueName)
                .uploadedAt(LocalDateTime.now())
                .room(room) // Link the image to the room
                .build();

        roomImageFile = fileRepository.save(roomImageFile);

        return mapToResponse(roomImageFile);
    }

    // Multiple files upload for a single Room
    @Override
    public List<FileResponse> uploadMultipleFiles(MultipartFile[] files, Long roomId) throws IOException {
        if (files == null || files.length == 0) {
            throw new FileStorageException("No files provided for upload.");
        }
        return List.of(files).stream()
                .map(file -> {
                    try {
                        return uploadFile(file, roomId);
                    } catch (IOException e) {
                        throw new FileStorageException("Error uploading file: " + file.getOriginalFilename(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    // Fetch all files for the current user
    @Override
    public List<FileResponse> getAllFiles() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return fileRepository.findByCreatedBy(currentUserId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Fetch a single file by ID
    @Override
    public FileResponse getFile(Long id) {
        RoomImage entity = fileRepository.findById(id)
                .orElseThrow(() -> new FileStorageException("File not found with id " + id));
        return mapToResponse(entity);
    }

    // Update file by ID
//    @Override
//    public FileResponse updateFile(Long id, MultipartFile newFile) throws IOException {
//        RoomImage entity = fileRepository.findById(id)
//                .orElseThrow(() -> new FileStorageException("File not found with id " + id));
//
//        // Delete old physical file
//        deletePhysicalFile(entity.getUniqueName());
//
//        // Save new file
//        String uniqueName = UUID.randomUUID() + "_" + newFile.getOriginalFilename();
//        Path targetLocation = getUploadPath().resolve(uniqueName);
//        Files.copy(newFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//
//        // Update DB metadata
//        entity.setOriginalName(newFile.getOriginalFilename());
//        entity.setUniqueName(uniqueName);
//        entity.setContentType(newFile.getContentType());
//        entity.setSize(newFile.getSize());
//        entity.setUrl("/files/" + uniqueName);
//        entity.setUploadedAt(LocalDateTime.now());
//
//        return mapToResponse(fileRepository.save(entity));
//    }

    // Delete file by ID
    @Override
    public void deleteFile(Long id) {
        RoomImage entity = fileRepository.findById(id)
                .orElseThrow(() -> new FileStorageException("File not found with id " + id));

        deletePhysicalFile(entity.getUniqueName());
        fileRepository.delete(entity);
    }

    // Delete physical file from storage
    private void deletePhysicalFile(String uniqueName) {
        try {
            Path filePath = getUploadPath().resolve(uniqueName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileStorageException("Could not delete file: " + uniqueName, e);
        }
    }

    // Map RoomImage entity to FileResponse DTO
    private FileResponse mapToResponse(RoomImage entity) {
        return FileResponse.builder()
                .id(entity.getId())
                .originalName(entity.getOriginalName())
                .url(entity.getUrl())
                .contentType(entity.getContentType())
                .size(entity.getSize())
                .uploadedAt(entity.getUploadedAt())
                .build();
    }
}
