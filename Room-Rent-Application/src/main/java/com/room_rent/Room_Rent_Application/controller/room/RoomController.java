package com.room_rent.Room_Rent_Application.controller.room;

import com.room_rent.Room_Rent_Application.controller.common.BaseController;
import com.room_rent.Room_Rent_Application.dto.room.RoomFilterRequest;
import com.room_rent.Room_Rent_Application.dto.room.RoomRequest;
import com.room_rent.Room_Rent_Application.dto.room.RoomResponse;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController extends BaseController {

    private final RoomService roomService;
    private final CustomMessageSource customMessageSource;


    @PostMapping(value = "/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createRoom(
            @ModelAttribute RoomRequest request,
            @RequestPart(name = "mediaFiles", required = false) MultipartFile[] mediaFiles,
            Principal principal) throws IOException {

        RoomResponse roomResponse = roomService.createRoom(request, mediaFiles, principal.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(successResponse(
                        customMessageSource.get(SUCCESS_SAVE, customMessageSource.get(ROOM)),
                        roomResponse
                ));
    }

    @PutMapping(value = "/admin/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateRoom(
            @PathVariable Long id,
            @ModelAttribute RoomRequest request,
            @RequestPart(value = "mediaFiles", required = false) MultipartFile[] mediaFiles,
            Principal principal) throws IOException {

        RoomResponse updatedRoom = roomService.updateRoom(id, request, mediaFiles, principal.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(successResponse(
                        customMessageSource.get(SUCCESS_UPDATE, customMessageSource.get(ROOM)),
                        updatedRoom
                ));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id, Principal principal) {
        roomService.deleteRoom(id, principal.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(successResponse(
                        customMessageSource.get(SUCCESS_DELETE, customMessageSource.get(ROOM)),
                        null
                ));
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getMyRooms(Principal principal) {
        List<RoomResponse> myRooms = roomService.getRoomsByAdmin(principal.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(successResponse(
                        customMessageSource.get(FETCHED_LIST, customMessageSource.get(ROOM)),
                        myRooms
                ));
    }

    @GetMapping("/super-admin/filter")
    public ResponseEntity<?> getAllRooms(@RequestBody RoomFilterRequest filter) {
        List<RoomResponse> allRooms = roomService.getAllRooms(filter);

        return ResponseEntity.status(HttpStatus.OK)
                .body(successResponse(
                        customMessageSource.get(FETCHED_LIST, customMessageSource.get(ROOM)),
                        allRooms
                ));
    }

}

