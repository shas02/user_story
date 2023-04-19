package com.user_story.api.service;

import com.user_story.api.dto.transaction.TransactionDto;

public interface FeeCalculator {
    Double calculateFee(TransactionDto transaction);
}
