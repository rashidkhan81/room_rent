package com.room_rent.Room_Rent_Application.service.room.impl;

import com.room_rent.Room_Rent_Application.config.fileConfig.FileStorageConfig;
import com.room_rent.Room_Rent_Application.dto.room.FileResponse;
import com.room_rent.Room_Rent_Application.exception.FileStorageException.FileStorageException;
import com.room_rent.Room_Rent_Application.model.room.Room;
import com.room_rent.Room_Rent_Application.repository.room.FileRepository;
import com.room_rent.Room_Rent_Application.service.room.FileService;
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
public class RoomServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileStorageConfig fileStorageConfig;

    private Path getUploadPath() {
        return Path.of(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();
    }

    @Override
    public FileResponse uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new FileStorageException("Cannot upload empty file.");
        }

        // Generate unique file name
        String uniqueName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetLocation = getUploadPath().resolve(uniqueName);

        // Ensure directory exists
        Files.createDirectories(targetLocation.getParent());

        // Copy file
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Save metadata in DB
        Room roomFile = Room.builder()
                .originalName(file.getOriginalFilename())
                .uniqueName(uniqueName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .url("/files/" + uniqueName)
                .uploadedAt(LocalDateTime.now())
                .build();

        roomFile = fileRepository.save(roomFile);

        return mapToResponse(roomFile);
    }

    @Override
    public List<FileResponse> uploadMultipleFiles(MultipartFile[] files) throws IOException {
        if (files == null || files.length == 0) {
            throw new FileStorageException("No files provided for upload.");
        }
        return List.of(files).stream()
                .map(file -> {
                    try {
                        return uploadFile(file);
                    } catch (IOException e) {
                        throw new FileStorageException("Error uploading file: " + file.getOriginalFilename(), e);
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public List<FileResponse> getAllFiles() {
        return fileRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FileResponse getFile(Long id) {
        Room entity = fileRepository.findById(id)
                .orElseThrow(() -> new FileStorageException("File not found with id " + id));
        return mapToResponse(entity);
    }

    @Override
    public FileResponse updateFile(Long id, MultipartFile newFile) throws IOException {
        Room entity = fileRepository.findById(id)
                .orElseThrow(() -> new FileStorageException("File not found with id " + id));

        // Delete old physical file
        deletePhysicalFile(entity.getUniqueName());

        // Save new file
        String uniqueName = UUID.randomUUID() + "_" + newFile.getOriginalFilename();
        Path targetLocation = getUploadPath().resolve(uniqueName);
        Files.copy(newFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Update DB metadata
        entity.setOriginalName(newFile.getOriginalFilename());
        entity.setUniqueName(uniqueName);
        entity.setContentType(newFile.getContentType());
        entity.setSize(newFile.getSize());
        entity.setUrl("/files/" + uniqueName);
        entity.setUploadedAt(LocalDateTime.now());

        return mapToResponse(fileRepository.save(entity));
    }

    @Override
    public void deleteFile(Long id) {
        Room entity = fileRepository.findById(id)
                .orElseThrow(() -> new FileStorageException("File not found with id " + id));

        deletePhysicalFile(entity.getUniqueName());
        fileRepository.delete(entity);
    }

    private void deletePhysicalFile(String uniqueName) {
        try {
            Path filePath = getUploadPath().resolve(uniqueName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileStorageException("Could not delete file: " + uniqueName, e);
        }
    }

    private FileResponse mapToResponse(Room entity) {
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
