package com.user_story.api.dto.client;

import com.user_story.api.data.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientDto {
    private Long id;
    private Date createDate;
    private Date lastModifiedDate;
    private Integer version;
    private String name;
    private String email;
    private List<Transaction> transactions;
}
