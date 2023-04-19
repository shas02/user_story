package com.user_story.api.dto.transaction;

import com.user_story.api.data.domain.Client;
import com.user_story.api.service.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDto {
    private Long id;
    private Date createDate;
    private Date lastModifiedDate;
    private Integer version;
    private Client client;
    private TransactionType type;
    private String description;
    private Double amount;
}
