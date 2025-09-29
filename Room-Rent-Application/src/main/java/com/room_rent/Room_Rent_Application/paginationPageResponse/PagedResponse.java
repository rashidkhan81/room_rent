    package com.room_rent.Room_Rent_Application.paginationPageResponse;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.util.List;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class PagedResponse<T> {
        private List<T> content;
        private PageInfo pageInfo;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class PageInfo {
            private int pageNumber;
            private int pageSize;
            private long totalElements;
            private int totalPages;
            private boolean last;
        }
    }

