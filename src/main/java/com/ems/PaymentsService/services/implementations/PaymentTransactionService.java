package com.ems.PaymentsService.services.implementations;

import com.ems.PaymentsService.dto.TransactionViewDTO;
import com.ems.PaymentsService.entity.PaymentTransaction;
import com.ems.PaymentsService.entity.UserBankAccount;
import com.ems.PaymentsService.enums.TransactionType;
import com.ems.PaymentsService.exceptions.custom.BusinessValidationException;
import com.ems.PaymentsService.exceptions.custom.DataNotFoundException;
import com.ems.PaymentsService.mapper.PaymentTransactionMapper;
import com.ems.PaymentsService.model.PaymentTransactionModel;
import com.ems.PaymentsService.repositories.PaymentTransactionRepository;
import com.ems.PaymentsService.repositories.UserBankAccountRepository;
import com.ems.PaymentsService.utility.constants.ErrorMessages;

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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        String currentDateTime = String.valueOf(LocalDateTime.now());
        PaymentTransaction entity = paymentTransactionMapper.toEntity(model);
        entity.setCreatedDate(currentDateTime);
        entity.setLastUpdatedDate(currentDateTime);

        entity = paymentTransactionRepository.save(entity);
        paymentTransactionRepository.flush();
        String transactionId = String.valueOf(entity.getId());
        model.setId(transactionId);
        log.info("Payment transaction created with ID: {}", transactionId);

        UserBankAccount userBankAccount = userBankAccountRepository.findById(Integer.parseInt(model.getBankId()))
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.BANK_ID_NOT_FOUND));

        if ("NOT_PAID".equals(model.getPaymentStatus()) || "PAY_CANCELLED".equals(model.getPaymentStatus())) {
            double amount = Double.parseDouble(model.getAmountPaid());
            userBankAccount.setAccountBalance(userBankAccount.getAccountBalance() + amount);
            userBankAccount.setLastUpdatedDate(currentDateTime);
            userBankAccountRepository.save(userBankAccount);
            log.info("Amount credited back to account for cancelled payment");
            cancelEventRegistration(model);
        } else {
            updateBankAccountBalance(userBankAccount, model);
            registerForEventInEventsService(model);
            log.info("Payment processed and event registration completed");
        }
        return paymentTransactionMapper.toModel(entity);
    }

    private void checkExistingRegistration(PaymentTransactionModel model) {
        String url = eventsServiceUrl + "/ems/events/registration";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("eventId", model.getEventId());
        requestBody.put("userId", model.getUserId());
        requestBody.put("createdBy", model.getCreatedBy());
        requestBody.put("paymentStatus", model.getPaymentStatus());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            log.info("Registration check completed successfully");
        } catch (Exception e) {
            if (e.getMessage().contains("already registered")) {
                throw new BusinessValidationException("User already registered for this event");
            }
            throw e;
        }
    }

    private void cancelEventRegistration(PaymentTransactionModel model) {
        String url = eventsServiceUrl + "/ems/events/registrations/cancel";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("transactionId", model.getId());
        requestBody.put("eventId", model.getEventId());
        requestBody.put("userId", model.getUserId());
        requestBody.put("createdBy", model.getCreatedBy());
        requestBody.put("paymentStatus", model.getPaymentStatus());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to cancel registration. Response status: {}", response.getStatusCode());
            throw new BusinessValidationException("Failed to cancel registration");
        }
        log.info("Successfully cancelled event registration. Response: {}", response.getBody());
    }

    private void validatePaymentTransaction(PaymentTransactionModel model) {
        log.debug("Validating payment transaction");

        if (model.getAmountPaid() == null || Double.parseDouble(model.getAmountPaid()) <= 0) {
            throw new BusinessValidationException(ErrorMessages.AMOUNT_VALIDATION);
        }

        if (model.getEventId() == null || model.getEventId().trim().isEmpty()) {
            throw new BusinessValidationException(ErrorMessages.EVENT_ID_NOT_FOUND);
        }

        if (model.getPaymentMode() == null) {
            throw new BusinessValidationException(ErrorMessages.PAYMENT_MODE_NOT_FOUND);
        }

        if (model.getTransactionType() == null) {
            throw new BusinessValidationException(ErrorMessages.TRANSACTION_TYPE_NOT_FOUND);
        }


        if (model.getPaymentStatus() == null) {
            throw new BusinessValidationException(ErrorMessages.PAYMENT_STATUS_NOT_FOUND);
        }

        log.debug("Payment transaction validation completed");
    }

    private void updateBankAccountBalance(UserBankAccount account, PaymentTransactionModel model) {
        double amount = Double.parseDouble(model.getAmountPaid());
        if (TransactionType.DEBIT.name().equalsIgnoreCase(model.getTransactionType())) {
            if (account.getAccountBalance() < amount) {
                throw new BusinessValidationException(ErrorMessages.INSUFFICIENT_BALANCE);
            }
            account.setAccountBalance(account.getAccountBalance() - amount);
        } else {
            account.setAccountBalance(account.getAccountBalance() + amount);
        }
        account.setLastUpdatedDate(String.valueOf(LocalDateTime.now()));
        userBankAccountRepository.save(account);
        log.info("Bank account balance updated for account ID: {}", account.getAccountId());
    }

    private void registerForEventInEventsService(PaymentTransactionModel model) {
        String url = eventsServiceUrl + "/ems/events/registration";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("transactionId", model.getId());
        requestBody.put("eventId", model.getEventId());
        requestBody.put("userId", model.getUserId());
        requestBody.put("createdBy", model.getCreatedBy());
        requestBody.put("paymentStatus", model.getPaymentStatus());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to register for event. Response status: {}", response.getStatusCode());
            throw new BusinessValidationException(ErrorMessages.FAILED_REGISTRATION);
        }
        log.info("Successfully registered for event. Response: {}", response.getBody());
    }

    public List<TransactionViewDTO> getAllTransactions() {
        log.info("Retrieving all payment transactions");
        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();

        if (transactions.isEmpty()) {
            log.error("No transactions found in the system");
            throw new DataNotFoundException(ErrorMessages.TRANSACTIONS_NOT_FOUND);
        }
        log.info("Found {} transactions", transactions.size());
        return transactions.stream()
                .map(paymentTransactionMapper::toModel)
                .map(TransactionViewDTO::new)
                .collect(Collectors.toList());
    }

}
