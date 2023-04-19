package com.user_story.api.service;

import com.user_story.api.data.domain.Client;
import com.user_story.api.data.domain.Transaction;
import com.user_story.api.data.repository.TransactionRepository;
import com.user_story.api.dto.transaction.TransactionCreateDto;
import com.user_story.api.dto.transaction.TransactionDto;
import com.user_story.api.dto.transaction.TransactionPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final ClientService clientService;

    public TransactionService(TransactionRepository transactionRepository, ClientService clientService) {
        this.transactionRepository = transactionRepository;
        this.clientService = clientService;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<TransactionDto> getClientsTransactions(Long clientId) {
        List<Transaction> transactions = transactionRepository.findAllByClientIdOrderByCreateDateDesc(clientId);
        return transactions.stream().map(transaction -> {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setId(transaction.getId());
            transactionDto.setCreateDate(transaction.getCreateDate());
            transactionDto.setLastModifiedDate(transaction.getLastModifiedDate());
            transactionDto.setVersion(transaction.getVersion());
            transactionDto.setClient(transaction.getClient());
            transactionDto.setType(transaction.getType());
            transactionDto.setDescription(transaction.getDescription());
            transactionDto.setAmount(transaction.getAmount());
            return transactionDto;
        }).toList();
    }

    public TransactionPage getClientsTransactionsPage(Long clientId, int page, int size) {
        Page<Transaction> transactions = transactionRepository.findAllByClientIdOrderByCreateDateDesc(clientId, PageRequest.of(page, size));
        TransactionPage transactionPage = new TransactionPage();
        transactionPage.setTransactions(transactions.getContent().stream().map(transaction -> {
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setId(transaction.getId());
            transactionDto.setCreateDate(transaction.getCreateDate());
            transactionDto.setLastModifiedDate(transaction.getLastModifiedDate());
            transactionDto.setVersion(transaction.getVersion());
            transactionDto.setClient(transaction.getClient());
            transactionDto.setType(transaction.getType());
            transactionDto.setDescription(transaction.getDescription());
            transactionDto.setAmount(transaction.getAmount());
            return transactionDto;
        }).collect(Collectors.toList()));
        transactionPage.setCurrentPage(transactions.getNumber());
        transactionPage.setIsLast(transactions.isLast());
        transactionPage.setTotalElements(transactions.getTotalElements());
        return transactionPage;
    }

    public TransactionDto getTransactionDtoById(Long id) {
        Transaction transaction = getTransactionById(id);
        if (transaction == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction does not exist!");
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setCreateDate(transaction.getCreateDate());
        transactionDto.setLastModifiedDate(transaction.getLastModifiedDate());
        transactionDto.setVersion(transaction.getVersion());
        transactionDto.setClient(transaction.getClient());
        transactionDto.setType(transaction.getType());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setAmount(transaction.getAmount());
        return transactionDto;
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public TransactionCreateDto createTransaction(TransactionCreateDto transactionCreateDto) {
        Transaction transaction = new Transaction();
        Client client = clientService.getClientById(transactionCreateDto.getClientId());
        if (client == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client does not exist!");;
        transaction.setClient(client);
        transaction.setType(transactionCreateDto.getType());
        transaction.setDescription(transactionCreateDto.getDescription());
        transaction.setAmount(transactionCreateDto.getAmount());
        final Transaction savedTransaction = transactionRepository.save(transaction);
        transactionCreateDto.setId(savedTransaction.getId());
        return transactionCreateDto;


    }

    public TransactionCreateDto updateTransaction(Long id, TransactionCreateDto transactionCreateDto) {
        Transaction existingTransaction = getTransactionById(id);
        if (existingTransaction == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction does not exist!");
        Client client = clientService.getClientById(transactionCreateDto.getClientId());
        existingTransaction.setClient(client);
        existingTransaction.setType(transactionCreateDto.getType());
        existingTransaction.setDescription(transactionCreateDto.getDescription());
        existingTransaction.setAmount(transactionCreateDto.getAmount());
        Transaction savedTransaction = transactionRepository.save(existingTransaction);
        transactionCreateDto.setId(savedTransaction.getId());
        return transactionCreateDto;
    }

    public boolean existById(Long id) {
        return transactionRepository.existsById(id);
    }

    public boolean deleteTransaction(Long id) {
        if (!existById(id)) return false;
        transactionRepository.deleteById(id);
        return true;
    }
}
