package com.user_story.api.data.repository;

import com.user_story.api.data.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByClientIdOrderByCreateDateDesc(Long clientId);

    Page<Transaction> findAllByClientIdOrderByCreateDateDesc(Long clientId, PageRequest pageRequest);
}
