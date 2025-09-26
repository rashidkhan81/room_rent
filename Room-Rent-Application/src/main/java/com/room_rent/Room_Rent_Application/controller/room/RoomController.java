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

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.FETCHED_LIST;
import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.ROOM;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController extends BaseController {

    private final RoomService roomService;
    private final CustomMessageSource customMessageSource;


    // Admin APIs
//    @PostMapping(value = "/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<RoomResponse> createRoom(
//            @ModelAttribute RoomRequest request,
//            @RequestPart("mediaFiles") MultipartFile[] mediaFiles,
//            Principal principal) {
//        return ResponseEntity.ok(roomService.createRoom(request, mediaFiles, principal.getName()));
//    }

//    @PostMapping(value = "/admin", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> createRoom(
//            @ModelAttribute RoomRequest request,
//            @RequestPart(name = "mediaFiles", required = false) MultipartFile[] mediaFiles,
//            Principal principal) throws IOException {
//
//       // log.info("Creating a new room for user: {}", principal.getName());
//
//        // Call your service to create room, handle files
//        RoomResponse roomResponse = roomService.createRoom(request, mediaFiles, principal.getName());
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(s(customMessageSource.get(FETCHED_LIST, customMessageSource.get(ROOM)),
//                        roomResponse));
//    }




    @PutMapping(value = "/admin/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @ModelAttribute RoomRequest request,
            @RequestPart(value = "mediaFiles", required = false) MultipartFile[] mediaFiles,
            Principal principal) throws IOException {
        return ResponseEntity.ok(roomService.updateRoom(id, request, mediaFiles, principal.getName()));
    }


    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id, Principal principal) {
        roomService.deleteRoom(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin")
    public ResponseEntity<List<RoomResponse>> getMyRooms(Principal principal) {
        return ResponseEntity.ok(roomService.getRoomsByAdmin(principal.getName()));
    }

    // Super Admin API
    @GetMapping("/super-admin/filter")
    public ResponseEntity<List<RoomResponse>> getAllRooms(@RequestBody RoomFilterRequest filter) {
        return ResponseEntity.ok(roomService.getAllRooms(filter));
    }
}

