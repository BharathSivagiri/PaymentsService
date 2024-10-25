package com.ems.PaymentsService.controller;

import com.ems.PaymentsService.dto.TransactionViewDTO;
import com.ems.PaymentsService.model.PaymentTransactionModel;
import com.ems.PaymentsService.repositories.PaymentTransactionRepository;
import com.ems.PaymentsService.services.implementations.PaymentTransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ems/events")
@Tag(name = "Payment Transactions API", description = "API for managing payment transactions")
public class PaymentTransactionController
{

    private static final Logger log = LoggerFactory.getLogger(PaymentTransactionController.class);

        @Autowired
        PaymentTransactionService paymentTransactionService;

        @Autowired
        PaymentTransactionRepository paymentTransactionRepository;

    @PostMapping("/payment/process")
    public ResponseEntity<Integer> processPayment(@RequestBody PaymentTransactionModel request) {
        PaymentTransactionModel response = paymentTransactionService.createPaymentTransaction(request);
        Integer transactionId = paymentTransactionRepository.findFirstByOrderByIdDesc().getId();
        return ResponseEntity.ok(transactionId);
    }

    @GetMapping("/transactions")
    @Operation(summary = "Get all payment transactions", description = "Retrieve a list of all payment transactions")
    public ResponseEntity<List<TransactionViewDTO>> getAllTransactions()
    {
        log.info("Fetching all payment transactions");
        List<TransactionViewDTO> transactions = paymentTransactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}

