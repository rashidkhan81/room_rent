package com.room_rent.Room_Rent_Application.service.room.impl;

import com.room_rent.Room_Rent_Application.dto.room.RoomFilterRequest;
import com.room_rent.Room_Rent_Application.dto.room.RoomRequest;
import com.room_rent.Room_Rent_Application.dto.room.RoomResponse;
import com.room_rent.Room_Rent_Application.model.User;
import com.room_rent.Room_Rent_Application.model.room.Room;
import com.room_rent.Room_Rent_Application.repository.UserRepository;
import com.room_rent.Room_Rent_Application.repository.room.RoomRepository;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Value("${app.upload.dir}")
    private  String uploadDir;

    @Override
    public RoomResponse createRoom(RoomRequest request, MultipartFile[] mediaFiles, String adminEmail) throws IOException {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        Room room = new Room();
        mapRequestToEntity(request, room);
        room.setCreatedBy(admin);

        List<String> mediaPaths = saveMediaFiles(mediaFiles);
        room.setMediaUrls(mediaPaths);
        System.out.println("Upload dir absolute path: " + new File(uploadDir).getAbsolutePath());


        roomRepository.save(room);

        return mapToResponse(room);
    }

    @Override
    public RoomResponse updateRoom(Long id, RoomRequest request, MultipartFile[] mediaFiles, String adminEmail) throws IOException {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // ensure only owner admin can update
        if (!room.getCreatedBy().getEmail().equals(adminEmail)) {
            throw new SecurityException("You can update only your own rooms!");
        }

        mapRequestToEntity(request, room);

        if (mediaFiles != null && mediaFiles.length > 0) {
            List<String> mediaPaths = saveMediaFiles(mediaFiles);
            room.setMediaUrls(mediaPaths);
        }

        roomRepository.save(room);
        return mapToResponse(room);
    }

    @Override
    public void deleteRoom(Long id, String adminEmail) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.getCreatedBy().getEmail().equals(adminEmail)) {
            throw new SecurityException("You can delete only your own rooms!");
        }

        roomRepository.delete(room);
    }

    @Override
    public List<RoomResponse> getRoomsByAdmin(String adminEmail) {
        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        return roomRepository.findByCreatedBy(admin).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<RoomResponse> getAllRooms(RoomFilterRequest filter) {
        return roomRepository.filterRooms(filter.location(),
                        filter.minPrice(),
                        filter.maxPrice()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ----------------- Helper Methods -----------------

    private void mapRequestToEntity(RoomRequest request, Room room) {
        room.setPrice(request.price());
        room.setWaterFacility(request.waterFacility());
        room.setContactNo(request.contactNo());
        room.setLocation(request.location());
        room.setBooking(request.booking());
        room.setShareType(request.shareType());
    }

    private RoomResponse mapToResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getPrice(),
                room.getWaterFacility(),
                room.getContactNo(),
                room.getLocation(),
                room.getBooking(),
                room.getShareType(),
                room.getMediaUrls(),
                room.getCreatedBy().getEmail()
        );
    }

    public List<String> saveMediaFiles(MultipartFile[] mediaFiles) throws IOException {
        List<String> filePaths = new ArrayList<>();

        if (mediaFiles != null && mediaFiles.length > 0) {
            // Ensure the directory exists
            Path dirPath = Paths.get(uploadDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            for (MultipartFile file : mediaFiles) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path filePath = dirPath.resolve(fileName);

                file.transferTo(filePath.toFile());

                filePaths.add("/uploads/rooms/" + fileName);
            }
        }


        return filePaths;
    }
}
