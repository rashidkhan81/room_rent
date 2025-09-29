package com.room_rent.Room_Rent_Application.service.room;

import com.room_rent.Room_Rent_Application.common.specification.RoomSpecification;
import com.room_rent.Room_Rent_Application.dto.file.FileResponse;
import com.room_rent.Room_Rent_Application.dto.room.RoomRequestProjection;
import com.room_rent.Room_Rent_Application.dto.room.RoomResponseProjection;
import com.room_rent.Room_Rent_Application.enums.room.RoomType;
import com.room_rent.Room_Rent_Application.exception.NotFoundException;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.model.files.RoomImage;
import com.room_rent.Room_Rent_Application.model.room.Room;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.NOT_FOUND;
import static com.room_rent.Room_Rent_Application.message.SuccessResponseConstant.ROOM;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final CustomMessageSource customMessageSource;
    @Override
    public Room saveRoom(RoomRequestProjection roomPojo, Long id) {
        Room room;

        if (Objects.nonNull(id)) {
            // update
            room = roomRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(
                            customMessageSource.get(NOT_FOUND, customMessageSource.get(ROOM))
                    ));
        } else {
            // create
            room = new Room();
        }

        // set common fields
        room.setTitle(roomPojo.getTitle());
        room.setDescription(roomPojo.getDescription());
        room.setPrice(roomPojo.getPrice());
        room.setContact(roomPojo.getContact());
        room.setAddress(roomPojo.getAddress());
        room.setRoomType(roomPojo.getRoomType());
        room.setUploadedAt(LocalDateTime.now());

        // handle images if provided
//        if (roomPojo.getUrl() != null) {
//            RoomImage image = RoomImage.builder()
//                    .originalName(roomPojo.getOriginalName())
//                    .uniqueName(roomPojo.getUniqueName())
//                    .contentType(roomPojo.getContentType())
//                    .size(roomPojo.getSize() != null ? roomPojo.getSize() : 0)
//                    .url(roomPojo.getUrl())
//                    .uploadedAt(LocalDateTime.now())
//                    .room(room)
//                    .build();
//
//            room.setImages(List.of(image));
//        }

        return roomRepository.save(room);
    }



    @Override
    public PagedResponse<RoomResponseProjection> getRooms(
            Pageable pageable,
            String city,
            String district,
            RoomType roomType,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        Specification<Room> spec = RoomSpecification.filterRooms(city, district, roomType, minPrice, maxPrice);

        Page<Room> roomsPage = roomRepository.findAll(spec, pageable);

        List<RoomResponseProjection> content = roomsPage.stream().map(room -> {
            List<FileResponse> images = room.getImages().stream()
                    .map(img -> new FileResponse(
                            img.getId(),
                            img.getOriginalName(),
                            img.getUrl(),
                            img.getContentType(),
                            img.getSize(),
                            img.getUploadedAt(),
                            img.getRoom().getCreatedBy()
                    ))
                    .toList();

            return new RoomResponseProjection(
                    room.getId(),
                    room.getTitle(),
                    room.getDescription(),
                    room.getPrice(),
                    room.getContact(),
                    room.getAddress(),
                    room.getRoomType(),
                    images,
                    room.getUploadedAt()
            );
        }).toList();

        return new PagedResponse<>(
                content,
                new PagedResponse.PageInfo(
                        roomsPage.getNumber(),
                        roomsPage.getSize(),
                        roomsPage.getTotalElements(),
                        roomsPage.getTotalPages(),
                        roomsPage.isLast()
                )
        );
    }



    @Override
    public RoomResponseProjection getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()
                        -> new NotFoundException(customMessageSource.get(NOT_FOUND,customMessageSource.get(ROOM))));

        List<FileResponse> images = room.getImages().stream()
                .map(img -> new FileResponse(
                        img.getId(),
                        img.getOriginalName(),
                        img.getUrl(),
                        img.getContentType(),
                        img.getSize(),
                        img.getUploadedAt(),
                        img.getRoom().getCreatedBy()
                ))
                .toList();

        return new RoomResponseProjection(
                room.getId(),
                room.getTitle(),
                room.getDescription(),
                room.getPrice(),
                room.getContact(),
                room.getAddress(),
                room.getRoomType(),
                images,
                room.getUploadedAt()
        );
    }

       @Override
       public void deleteRoom(Long id){
         roomRepository.deleteById(id);

       }

}
