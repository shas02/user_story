package com.user_story.api.controller;

import com.user_story.api.dto.transaction.TransactionDto;
import com.user_story.api.service.CalculationService;
import com.user_story.api.service.ClientService;
import com.user_story.api.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/calculation/calculateFee")
public class CalculationController {
    private final ClientService clientService;
    private final TransactionService transactionService;
    private final CalculationService calculationService;

    public CalculationController(ClientService clientService, TransactionService transactionService, CalculationService calculationService) {
        this.clientService = clientService;
        this.transactionService = transactionService;
        this.calculationService = calculationService;
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Double> calculateTransactionFee(@PathVariable Long transactionId) {
        TransactionDto transaction = transactionService.getTransactionDtoById(transactionId);
        Double fee = calculationService.calculateFee(transaction);
        return ResponseEntity.ok(fee);
    }

    @GetMapping("/calculateTotalFee/client/{clientId}")
    public ResponseEntity<Double> calculateTotalClientFee(@PathVariable Long clientId) {
        if (!clientService.existsById(clientId)) return ResponseEntity.notFound().build();
        List<TransactionDto> transactions = transactionService.getClientsTransactions(clientId);
        Double fee = calculationService.calculateTotalFee(transactions);
        return ResponseEntity.ok(fee);
    }

}
