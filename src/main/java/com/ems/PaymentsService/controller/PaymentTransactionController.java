package com.ems.PaymentsService.controller;

import com.ems.PaymentsService.model.PaymentTransactionModel;
import com.ems.PaymentsService.services.implementations.PaymentTransactionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/ems/events")
@Tag(name = "Payment Transactions API", description = "API for managing payment transactions")
public class PaymentTransactionController {

    private static final Logger log = LoggerFactory.getLogger(PaymentTransactionController.class);

        @Autowired
        private PaymentTransactionService paymentTransactionService;

        @Autowired
        private RestTemplate restTemplate;

        @Value("${events.service.url}")
        private String eventsServiceUrl;

    @PostMapping("/registration")
    public ResponseEntity<String> registerForEvent(@RequestBody PaymentTransactionModel paymentTransaction) {
        log.info("Received registration request for event ID: {}", paymentTransaction.getEventId());

        PaymentTransactionModel createdTransaction = paymentTransactionService.createPaymentTransaction(paymentTransaction);

        log.info("Payment transaction created with ID: {}", createdTransaction.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Successfully registered for event with ID: " + paymentTransaction.getEventId() +
                        ". Transaction ID: " + createdTransaction.getId());
    }

}

