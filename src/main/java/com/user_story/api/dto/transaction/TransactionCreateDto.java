package com.user_story.api.dto.transaction;

import com.user_story.api.service.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionCreateDto {
    private Long Id;
    private Long clientId;
    private TransactionType type;
    private String description;
    private Double amount;
}
