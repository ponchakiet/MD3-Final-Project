package re.edu.quan_ly_thuc_tap.util.helper;

import org.springframework.data.domain.Page;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PaginationMeta;

public class PageResponseHelper {
    public static <T> PageResponse<T> toPageResponse(Page<T> page) {
        PaginationMeta pagination = PaginationMeta.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .build();

        return PageResponse.<T>builder()
                .items(page.getContent())
                .pagination(pagination)
                .build();
    }
}
