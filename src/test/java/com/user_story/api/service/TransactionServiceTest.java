package com.user_story.api.service;

import com.user_story.api.data.domain.Client;
import com.user_story.api.data.domain.Transaction;
import com.user_story.api.data.repository.TransactionRepository;
import com.user_story.api.dto.transaction.TransactionCreateDto;
import com.user_story.api.dto.transaction.TransactionDto;
import com.user_story.api.dto.transaction.TransactionPage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private TransactionService transactionService;

    private static final Long CLIENT_ID = 1L;
    private static final Long TRANSACTION_ID = 1L;

    @Test
    public void testGetAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        when(transactionRepository.findAll()).thenReturn(transactions);
        List<Transaction> result = transactionService.getAllTransactions();
        assertEquals(result, transactions);
        verify(transactionRepository).findAll();
    }

    @Test
    public void testGetClientsTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);
        transaction.setClient(new Client());
        transactions.add(transaction);
        when(transactionRepository.findAllByClientIdOrderByCreateDateDesc(CLIENT_ID)).thenReturn(transactions);

        List<TransactionDto> result = transactionService.getClientsTransactions(CLIENT_ID);

        assertEquals(transactions.size(), result.size());
        verify(transactionRepository).findAllByClientIdOrderByCreateDateDesc(CLIENT_ID);
    }

    @Test
    public void testGetClientsTransactionsPage() {
        int page = 0;
        int size = 10;
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        Page<Transaction> transactionPage = new PageImpl<>(transactions);
        when(transactionRepository.findAllByClientIdOrderByCreateDateDesc(CLIENT_ID, PageRequest.of(page, size))).thenReturn(transactionPage);

        TransactionPage result = transactionService.getClientsTransactionsPage(CLIENT_ID, page, size);
        assertEquals(transactions.size(), result.getTransactions().size());
        assertEquals(transactionPage.getNumber(), result.getCurrentPage());
        assertEquals(transactionPage.isLast(), result.getIsLast());
        assertEquals(transactionPage.getTotalElements(), result.getTotalElements());
        verify(transactionRepository).findAllByClientIdOrderByCreateDateDesc(CLIENT_ID, PageRequest.of(page, size));
    }

    @Test
    public void testGetTransactionDtoById() {
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);
        transaction.setClient(new Client());
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(transaction));
        TransactionDto result = transactionService.getTransactionDtoById(TRANSACTION_ID);
        assertEquals(transaction.getId(), result.getId());
        verify(transactionRepository).findById(TRANSACTION_ID);
    }

    @Test
    public void testGetTransactionDtoByIdNotFound() {
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> transactionService.getTransactionDtoById(TRANSACTION_ID));
    }

    @Test
    public void testGetTransactionById() {
        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);
        transaction.setClient(new Client());
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.of(transaction));
        Transaction result = transactionService.getTransactionById(TRANSACTION_ID);
        assertEquals(transaction, result);
        verify(transactionRepository).findById(TRANSACTION_ID);
    }

    @Test
    public void testGetTransactionByIdNotFound() {
        when(transactionRepository.findById(TRANSACTION_ID)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> transactionService.getTransactionDtoById(TRANSACTION_ID));
    }

    @Test
    void testCreateTransaction() {
        long clientId = 1L;

        TransactionCreateDto transactionCreateDto = new TransactionCreateDto();
        transactionCreateDto.setClientId(clientId);

        Client client = new Client();
        client.setId(clientId);
        when(clientService.getClientById(anyLong())).thenReturn(client);

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(1L);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionCreateDto result = transactionService.createTransaction(transactionCreateDto);

        assertNotNull(result.getId());
        assertEquals(savedTransaction.getId(), result.getId());
        verify(clientService, times(1)).getClientById(clientId);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransactionNotFound() {
        long clientId = 1L;
        TransactionCreateDto transactionCreateDto = new TransactionCreateDto();
        transactionCreateDto.setClientId(clientId);
        when(clientService.getClientById(anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> transactionService.createTransaction(transactionCreateDto));
        verify(clientService, times(1)).getClientById(clientId);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    public void testUpdateTransaction() {
        Long id = 1L;
        Transaction existingTransaction = new Transaction();
        existingTransaction.setId(id);
        existingTransaction.setType(TransactionType.TRANSFER);
        existingTransaction.setDescription("Description1");
        existingTransaction.setAmount(100.0);

        TransactionCreateDto transactionCreateDto = new TransactionCreateDto();
        transactionCreateDto.setClientId(id);
        transactionCreateDto.setType(TransactionType.DEPOSIT);
        transactionCreateDto.setDescription("Description2");
        transactionCreateDto.setAmount(200.0);

        Client client = new Client();
        client.setId(id);

        when(transactionRepository.findById((anyLong()))).thenReturn(Optional.of(existingTransaction));
        when(clientService.getClientById(anyLong())).thenReturn(client);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(existingTransaction);

        TransactionCreateDto updatedTransaction = transactionService.updateTransaction(id, transactionCreateDto);

        assertEquals(existingTransaction.getId(), updatedTransaction.getId());
        assertEquals(transactionCreateDto.getType(), updatedTransaction.getType());
        assertEquals(transactionCreateDto.getDescription(), updatedTransaction.getDescription());
        assertEquals(transactionCreateDto.getAmount(), updatedTransaction.getAmount());
    }

    @Test
    public void testExistById() {
        Long id = 1L;
        when(transactionRepository.existsById(id)).thenReturn(true);
        boolean result = transactionService.existById(id);
        assertTrue(result);
    }

    @Test
    public void testDeleteTransaction() {
        Long id = 1L;
        when(transactionRepository.existsById(id)).thenReturn(true);
        boolean result = transactionService.deleteTransaction(id);
        assertTrue(result);
        verify(transactionRepository, times(1)).deleteById(id);
    }

}
