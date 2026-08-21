package re.edu.quan_ly_thuc_tap.dto.response.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationMeta {
    private int currentPage;   // page hiện tại (tính từ 0)
    private int pageSize;      // số item mỗi trang
    private int totalPages;    // tổng số trang
    private long totalItems;   // tổng số item
}