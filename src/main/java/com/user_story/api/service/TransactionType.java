package com.user_story.api.service;

import com.user_story.api.dto.transaction.TransactionDto;

public enum TransactionType implements FeeCalculator {
    DEPOSIT {
        @Override
        public Double calculateFee(TransactionDto transaction) {
            return 0.0;
        }
    },
    WITHDRAWAL {
        @Override
        public Double calculateFee(TransactionDto transaction) {
            return transaction.getAmount() * 0.01; /*1% fee for withdrawals*/
        }
    },
    TRANSFER {
        @Override
        public Double calculateFee(TransactionDto transaction) {
            Double amount = transaction.getAmount();
            // If transfer amount is greater than 1000, 2% fee
            if (amount > 1000) return amount * 0.02;
            return amount * 0.01; /*Otherwise, 1% fee*/
        }
    }
}
