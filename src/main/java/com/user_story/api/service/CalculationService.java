package com.user_story.api.service;

import com.user_story.api.dto.transaction.TransactionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class CalculationService {
    public double calculateFee(TransactionDto transaction) {
        return transaction.getType().calculateFee(transaction);
    }

    public double calculateTotalFee(List<TransactionDto> transactions) {
        return transactions.stream().map(this::calculateFee).reduce(0.0, Double::sum);
    }

}
