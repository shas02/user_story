package com.user_story.api.controller;

import com.user_story.api.dto.transaction.TransactionDto;
import com.user_story.api.service.CalculationService;
import com.user_story.api.service.TransactionService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.user_story.api.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CalculationControllerTest {

    @Mock
    private ClientService clientService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CalculationService calculationService;

    @InjectMocks
    private CalculationController calculationController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(calculationController).build();
    }

    @Test
    public void testCalculateTransactionFee() throws Exception {
        long transactionId = 1L;
        TransactionDto transaction = new TransactionDto();
        when(transactionService.getTransactionDtoById(transactionId)).thenReturn(transaction);

        double fee = 1.0;
        when(calculationService.calculateFee(transaction)).thenReturn(fee);

        mockMvc.perform(get("/calculation/calculateFee/{transactionId}", transactionId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(fee)));
    }

    @Test
    public void testCalculateTotalClientFee() throws Exception {
        long clientId = 1L;
        when(clientService.existsById(clientId)).thenReturn(true);

        List<TransactionDto> transactions = new ArrayList<>();
        when(transactionService.getClientsTransactions(clientId)).thenReturn(transactions);

        double fee = 3.0;
        when(calculationService.calculateTotalFee(transactions)).thenReturn(fee);

        mockMvc.perform(get("/calculation/calculateFee/calculateTotalFee/client/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(fee)));
    }

    @Test
    public void testCalculateTotalClientFeeNotFound() throws Exception {
        long clientId = 1L;
        when(clientService.existsById(clientId)).thenReturn(false);
        mockMvc.perform(get("/calculation/calculateFee/calculateTotalFee/client/{clientId}", clientId))
                .andExpect(status().isNotFound());
    }
}

