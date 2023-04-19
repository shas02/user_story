package com.user_story.api.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.user_story.api.dto.transaction.TransactionDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculationServiceTest {

    @Mock
    private TransactionDto transactionDto;
    @InjectMocks
    private CalculationService calculationService;

    @Test
    public void testCalculateFeeForWITHDRAWALTransaction() {
        when(transactionDto.getType()).thenReturn(TransactionType.WITHDRAWAL);
        when(transactionDto.getAmount()).thenReturn(100.0);
        double fee = calculationService.calculateFee(transactionDto);
        assertEquals(1.0, fee);
    }

    @Test
    public void testCalculateFeeForTRANSFERTransaction() {
        when(transactionDto.getType()).thenReturn(TransactionType.TRANSFER);
        when(transactionDto.getAmount()).thenReturn(100.0);
        double fee = calculationService.calculateFee(transactionDto);
        assertEquals(1.0, fee);
        when(transactionDto.getAmount()).thenReturn(1001.0);
        fee = calculationService.calculateFee(transactionDto);
        assertEquals(20.02, fee);
    }

    @Test
    public void testCalculateTotalFee() {
        TransactionDto depositTransaction = new TransactionDto();
        depositTransaction.setAmount(100.0);
        depositTransaction.setType(TransactionType.DEPOSIT);

        TransactionDto withdrawalTransaction = new TransactionDto();
        withdrawalTransaction.setAmount(100.0);
        withdrawalTransaction.setType(TransactionType.WITHDRAWAL);

        TransactionDto transferTransaction = new TransactionDto();
        transferTransaction.setAmount(100.0);
        transferTransaction.setType(TransactionType.TRANSFER);

        List<TransactionDto> transactions = new ArrayList<>();
        transactions.add(depositTransaction);
        transactions.add(withdrawalTransaction);
        transactions.add(transferTransaction);

        double totalFee = calculationService.calculateTotalFee(transactions);

        assertEquals(2.0, totalFee);
    }
}
