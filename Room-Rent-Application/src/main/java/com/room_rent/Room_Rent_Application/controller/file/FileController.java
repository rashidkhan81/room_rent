package com.room_rent.Room_Rent_Application.controller.file;

import com.room_rent.Room_Rent_Application.common.BaseController;
import com.room_rent.Room_Rent_Application.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.*;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController extends BaseController {

    private final FileService fileService;

    // Upload single file for a specific room
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam("roomId") Long roomId
    ) throws IOException {
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_SAVE, customMessageSource.get(ROOM)),
                        fileService.uploadFile(file, roomId)
                )
        );
    }

    // Upload multiple files for a specific room
    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMultiple(
            @RequestPart("files") MultipartFile[] files,
            @RequestParam("roomId") Long roomId
    ) throws IOException {
        return ResponseEntity.ok(
                successResponse(
                        customMessageSource.get(SUCCESS_SAVE, customMessageSource.get(ROOM)),
                        fileService.uploadMultipleFiles(files, roomId)
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
