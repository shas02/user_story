package com.user_story.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user_story.api.dto.transaction.TransactionCreateDto;
import com.user_story.api.dto.transaction.TransactionDto;
import com.user_story.api.dto.transaction.TransactionPage;
import com.user_story.api.service.TransactionService;
import com.user_story.api.service.TransactionType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.user_story.api.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Mock
    private ClientService clientService;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void testGetTransactionDtoById() throws Exception {
        TransactionDto expectedTransaction = new TransactionDto();
        expectedTransaction.setId(1L);
        expectedTransaction.setType(TransactionType.DEPOSIT);
        expectedTransaction.setAmount(1.0);
        when(transactionService.getTransactionDtoById(1L)).thenReturn(expectedTransaction);

        mockMvc.perform(get("/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedTransaction.getId().intValue())))
                .andExpect(jsonPath("$.type", is(expectedTransaction.getType().name())))
                .andExpect(jsonPath("$.amount", is(expectedTransaction.getAmount())));
    }

    @Test
    public void testGetTransactionDtoByIdNotFound() throws Exception {
        when(transactionService.getTransactionDtoById(1L)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction does not exist!"));
        mockMvc.perform(get("/transactions/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetClientsTransactionsPage() throws Exception {
        long clientId = 1L;
        int page = 0;
        int size = 10;
        TransactionPage expectedPage = new TransactionPage();
        expectedPage.setTransactions(new ArrayList<>());
        expectedPage.setCurrentPage(1);
        expectedPage.setIsLast(true);
        when(clientService.existsById(clientId)).thenReturn(true);
        when(transactionService.getClientsTransactionsPage(clientId, page, size)).thenReturn(expectedPage);

        mockMvc.perform(get("/transactions/client/{clientId}", clientId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactions", hasSize(expectedPage.getTransactions().size())))
                .andExpect(jsonPath("$.currentPage", is(expectedPage.getCurrentPage())))
                .andExpect(jsonPath("$.isLast", is(expectedPage.getIsLast())));
    }

    @Test
    public void testGetTransactionsByClientIdNotFound() throws Exception {
        long clientId = 1L;
        int page = 0;
        int size = 10;
        when(clientService.existsById(clientId)).thenReturn(false);

        mockMvc.perform(get("/transactions/client/{clientId}", clientId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTransaction() throws Exception {
        TransactionCreateDto request = new TransactionCreateDto();
        request.setClientId(1L);
        request.setId(1L);
        request.setType(TransactionType.DEPOSIT);
        request.setAmount(1.0);
        when(transactionService.createTransaction(request)).thenReturn(request);

        mockMvc.perform(post("/transactions")
                        .content(new ObjectMapper().writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(request.getId().intValue())))
                .andExpect(jsonPath("$.clientId", is(request.getClientId().intValue())))
                .andExpect(jsonPath("$.type", is(request.getType().name())))
                .andExpect(jsonPath("$.amount", is(request.getAmount())));
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        long id = 1L;
        TransactionCreateDto request = new TransactionCreateDto();
        request.setId(1L);
        request.setClientId(1L);
        request.setType(TransactionType.WITHDRAWAL);
        request.setAmount(1.0);

        when(transactionService.updateTransaction(id, request)).thenReturn(request);
        mockMvc.perform(put("/transactions/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(request.getId().intValue())))
                .andExpect(jsonPath("$.type", is(request.getType().name())))
                .andExpect(jsonPath("$.amount", is(request.getAmount())));
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        long id = 1L;
        when(transactionService.deleteTransaction(id)).thenReturn(true);

        mockMvc.perform(delete("/transactions/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteTransactionNotFound() throws Exception {
        long id = 1L;
        when(transactionService.deleteTransaction(id)).thenReturn(false);

        mockMvc.perform(delete("/transactions/{id}", id))
                .andExpect(status().isNotFound());
    }
}
