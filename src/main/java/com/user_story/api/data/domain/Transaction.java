package com.user_story.api.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.user_story.api.service.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date createDate;

    @UpdateTimestamp
    private Date lastModifiedDate;

    @Version
    private Integer version;

    @ManyToOne(/*fetch = FetchType.LAZY*/)
    @JsonIgnore
    private Client client;

//    private Long clientId;

    private TransactionType type;

    private String description;

    private Double amount;
}
