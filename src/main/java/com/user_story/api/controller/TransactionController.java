package com.user_story.api.controller;

import com.user_story.api.dto.transaction.TransactionCreateDto;
import com.user_story.api.dto.transaction.TransactionDto;
import com.user_story.api.dto.transaction.TransactionPage;
import com.user_story.api.service.ClientService;
import com.user_story.api.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final ClientService clientService;
    private final TransactionService transactionService;

    public TransactionController(ClientService clientService, TransactionService transactionService) {
        this.clientService = clientService;
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long id) {
        TransactionDto transaction = transactionService.getTransactionDtoById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<TransactionPage> getTransactionsByClientId(@PathVariable Long clientId,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size) {
        if (!clientService.existsById(clientId)) return ResponseEntity.notFound().build();
        TransactionPage transactionsPage = transactionService.getClientsTransactionsPage(clientId, page, size);
        return ResponseEntity.ok(transactionsPage);
    }

    @PostMapping
    public ResponseEntity<TransactionCreateDto> createTransaction(@RequestBody TransactionCreateDto transaction) {
        TransactionCreateDto createdTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionCreateDto> updateTransaction(@PathVariable Long id, @RequestBody TransactionCreateDto transaction) {
        TransactionCreateDto updatedTransaction = transactionService.updateTransaction(id, transaction);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        if (transactionService.deleteTransaction(id)) return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

}
