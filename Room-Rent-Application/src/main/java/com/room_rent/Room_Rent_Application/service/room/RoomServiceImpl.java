package com.room_rent.Room_Rent_Application.service.room;

import com.room_rent.Room_Rent_Application.common.specification.RoomSpecification;
import com.room_rent.Room_Rent_Application.dto.file.FileResponse;
import com.room_rent.Room_Rent_Application.dto.room.RoomRequestProjection;
import com.room_rent.Room_Rent_Application.dto.room.RoomResponseProjection;
import com.room_rent.Room_Rent_Application.enums.room.RoomType;
import com.room_rent.Room_Rent_Application.exception.NotFoundException;
import com.room_rent.Room_Rent_Application.exception.UnauthorizedException;
import com.room_rent.Room_Rent_Application.jwtTokenUtils.JwtTokenUtil;
import com.room_rent.Room_Rent_Application.message.CustomMessageSource;
import com.room_rent.Room_Rent_Application.model.room.Room;
import com.room_rent.Room_Rent_Application.paginationPageResponse.PagedResponse;
import com.room_rent.Room_Rent_Application.paginationPageResponse.ResponseUtils;
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
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final CustomMessageSource customMessageSource;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() ->
                new NotFoundException(customMessageSource.get(NOT_FOUND, customMessageSource.get(ROOM))));
    }


    @Override
    public Room saveRoom(RoomRequestProjection roomPojo, Long id) {
        Room room;

        if (Objects.nonNull(id)) {
            // update
            room = roomRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(
                            customMessageSource.get(NOT_FOUND, customMessageSource.get(ROOM))
                    ));

            // optional: allow update only if logged-in user is the owner
            if (!room.getCreatedByUser().getId().equals(jwtTokenUtil.getUserIdFromToken())) {
                throw new UnauthorizedException("You are not allowed to update this room");
            }

        } else {
            // create
            room = new Room();
            room.setCreatedByUser(jwtTokenUtil.getLoggedInUser()); // set creator from JWT
            room.setUploadedAt(LocalDateTime.now());
        }

        // set common fields
        room.setTitle(roomPojo.getTitle());
        room.setDescription(roomPojo.getDescription());
        room.setPrice(roomPojo.getPrice());
        room.setContact(roomPojo.getContact());
        room.setAddress(roomPojo.getAddress());
        room.setRoomType(roomPojo.getRoomType());

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
        Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
        boolean isAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

        // restrict to own rooms if not super admin
        if (!isAdmin) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("createdByUser").get("id"), loggedInUserId));
        }

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
    public PagedResponse<RoomResponseProjection> getPublicRooms(
            Pageable pageable,
            String city,
            String district,
            RoomType roomType,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        Specification<Room> spec = RoomSpecification.filterRooms(city, district, roomType, minPrice, maxPrice);

        Page<Room> roomsPage = roomRepository.findAll(spec, pageable);

        return ResponseUtils.buildPagedResponse(roomsPage, room -> {
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
        });
    }


    @Override
    public RoomResponseProjection getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(()
                        -> new NotFoundException(customMessageSource.get(NOT_FOUND, customMessageSource.get(ROOM))));

        Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
        boolean isAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

        if (!isAdmin && !room.getCreatedByUser().getId().equals(loggedInUserId)) {
            throw new UnauthorizedException("You are not allowed to access this room");
        }
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
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(customMessageSource.get(NOT_FOUND, customMessageSource.get(ROOM))));

        Long loggedInUserId = jwtTokenUtil.getUserIdFromToken();
        boolean isAdmin = jwtTokenUtil.hasRole("ROLE_SUPER_ADMIN");

        if (!isAdmin && !room.getCreatedByUser().getId().equals(loggedInUserId)) {
            throw new UnauthorizedException("You are not allowed to delete this room");
        }

        roomRepository.delete(room);
    }



    //recommendation

    // Simple popularity-based recommendation
//    public List<RoomResponseProjection> recommendTopRatedRooms() {
//        return roomRepository.findTop5ByOrderByAverageRatingDesc();
//    }

}
