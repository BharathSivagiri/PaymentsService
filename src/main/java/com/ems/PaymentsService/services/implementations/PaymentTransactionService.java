package com.ems.PaymentsService.services.implementations;

import com.ems.PaymentsService.entity.PaymentTransaction;
import com.ems.PaymentsService.entity.UserBankAccount;
import com.ems.PaymentsService.enums.TransactionType;
import com.ems.PaymentsService.exceptions.custom.BusinessValidationException;
import com.ems.PaymentsService.exceptions.custom.DataNotFoundException;
import com.ems.PaymentsService.mapper.PaymentTransactionMapper;
import com.ems.PaymentsService.model.PaymentTransactionModel;
import com.ems.PaymentsService.repositories.PaymentTransactionRepository;
import com.ems.PaymentsService.repositories.UserBankAccountRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentTransactionService {
    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    private UserBankAccountRepository userBankAccountRepository;

    @Autowired
    private PaymentTransactionMapper paymentTransactionMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${events.service.url}")
    private String eventsServiceUrl;

    @Transactional
    public PaymentTransactionModel createPaymentTransaction(PaymentTransactionModel model) {
        log.info("Creating payment transaction for event ID: {}", model.getEventId());
        validatePaymentTransaction(model);

        PaymentTransaction entity = paymentTransactionMapper.toEntity(model);
        entity = paymentTransactionRepository.save(entity);

        String transactionId = String.valueOf(entity.getId());
        model.setId(transactionId);

        log.info("Payment transaction created with ID: {}", transactionId);

        registerForEventInEventsService(model);

        UserBankAccount userBankAccount = userBankAccountRepository.findById(Integer.parseInt(model.getBankId()))
                .orElseThrow(() -> new DataNotFoundException("Bank account not found for ID: " + model.getBankId()));

        updateBankAccountBalance(userBankAccount, model);

        entity.setCreatedDate(String.valueOf(LocalDateTime.now()));
        entity.setLastUpdatedDate(String.valueOf(LocalDateTime.now()));
        entity = paymentTransactionRepository.save(entity);

        PaymentTransactionModel response = paymentTransactionMapper.toModel(entity);
        log.info("Payment transaction process completed for ID: {}", response.getId());
        return response;
    }

    private void validatePaymentTransaction(PaymentTransactionModel model) {
        log.debug("Validating payment transaction");

        if (model.getAmountPaid() == null || Double.parseDouble(model.getAmountPaid()) <= 0) {
            throw new BusinessValidationException("Amount paid must be greater than zero");
        }

        if (model.getEventId() == null || model.getEventId().trim().isEmpty()) {
            throw new BusinessValidationException("Event ID is required");
        }

        if (model.getPaymentMode() == null) {
            throw new BusinessValidationException("Payment mode is required");
        }

        if (model.getTransactionType() == null) {
            throw new BusinessValidationException("Transaction type is required");
        }


        if (model.getPaymentStatus() == null) {
            throw new BusinessValidationException("Payment status is required");
        }

        log.debug("Payment transaction validation completed");
    }

    private void updateBankAccountBalance(UserBankAccount account, PaymentTransactionModel model) {
        double amount = Double.parseDouble(model.getAmountPaid());
        if (TransactionType.DEBIT.name().equalsIgnoreCase(model.getTransactionType())) {
            if (account.getAccountBalance() < amount) {
                throw new BusinessValidationException("Insufficient balance in the account");
            }
            account.setAccountBalance(account.getAccountBalance() - amount);
        } else {
            account.setAccountBalance(account.getAccountBalance() + amount);
        }
        account.setLastUpdatedDate(String.valueOf(LocalDateTime.now()));
        userBankAccountRepository.save(account);
        log.info("Bank account balance updated for account ID: {}", account.getAccountId());
    }
    private void storePaymentDataInEventsService(PaymentTransactionModel model) {
        String url = eventsServiceUrl + "/api/events/" + model.getEventId() + "/registration";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PaymentTransactionModel> request = new HttpEntity<>(model, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Payment data successfully stored in EventsService for event ID: {}", model.getEventId());
            } else {
                log.error("Failed to store payment data in EventsService. Status code: {}", response.getStatusCodeValue());
                throw new BusinessValidationException("Failed to update event with payment information");
            }
        } catch (Exception e) {
            log.error("Error occurred while storing payment data in EventsService", e);
            throw new BusinessValidationException("Failed to communicate with EventsService");
        }
    }

    private void registerForEventInEventsService(PaymentTransactionModel model) {
        String url = eventsServiceUrl + "/ems/events/registration";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("transactionId", model.getId());
        requestBody.put("eventId", model.getEventId());
        requestBody.put("createdBy", model.getCreatedBy());

        log.info("Sending registration request to EventsService - transactionId: {}, eventId: {}",
                model.getId(), model.getEventId());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to register for event. Response status: {}", response.getStatusCode());
            throw new BusinessValidationException("Failed to register for event");
        }

        log.info("Successfully registered for event. Response: {}", response.getBody());
    }
}
