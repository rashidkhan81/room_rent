package com.room_rent.Room_Rent_Application.paginationPageResponse;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

public class ResponseUtils {
    public static <T, U> PagedResponse<U> buildPagedResponse(Page<T> page, Function<T, U> mapper) {
        List<U> content = page.stream().map(mapper).toList();
        return new PagedResponse<>(
                content,
                new PagedResponse.PageInfo(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages(),
                        page.isLast()
                )
        );
    }
}
