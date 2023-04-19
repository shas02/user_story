package com.user_story.api.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionPage {
    private Integer currentPage;
    private Boolean isLast;
    private Long totalElements;
    private List<TransactionDto> transactions;
}
