package com.room_rent.Room_Rent_Application.controller.room;

import com.room_rent.Room_Rent_Application.common.BaseController;
import com.room_rent.Room_Rent_Application.service.room.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.*;


@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController extends BaseController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_SAVE, customMessageSource.get(ROOM)),
                        fileService.uploadFile(file)
                )
        );
    }


    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMultiple(@RequestPart("files") MultipartFile[] files) throws IOException {
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_SAVE, customMessageSource.get(ROOM)),
                        fileService.uploadMultipleFiles(files)
                )
        );
    }


    @GetMapping
    public ResponseEntity<?> getAllFiles() {
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(FETCHED_LIST, customMessageSource.get(ROOM)),
                        fileService.getAllFiles()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFile(@PathVariable Long id) {
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(FETCHED, customMessageSource.get(ROOM)),
                        fileService.getFile(id)
                )
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateFile(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_UPDATE, customMessageSource.get(ROOM)),
                        fileService.updateFile(id, file)
                )
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_DELETE, customMessageSource.get(ROOM)),
                        null
                )
        );
    }
}
