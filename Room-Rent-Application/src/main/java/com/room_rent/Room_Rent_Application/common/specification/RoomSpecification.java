package com.room_rent.Room_Rent_Application.common.specification;

import com.room_rent.Room_Rent_Application.enums.room.RoomType;
import com.room_rent.Room_Rent_Application.model.room.Room;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RoomSpecification {

    public static Specification<Room> filterRooms(
            String city,
            String district,
            RoomType roomType,
            BigDecimal minPrice,
            BigDecimal maxPrice
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (city != null && !city.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("city")), "%" + city.toLowerCase() + "%"));
            }
            if (district != null && !district.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("district")), "%" + district.toLowerCase() + "%"));
            }
            if (roomType != null) {
                predicates.add(cb.equal(root.get("roomType"), roomType));
            }
            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

