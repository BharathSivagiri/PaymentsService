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
import com.ems.PaymentsService.utility.constants.AppConstants;
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
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.BANK_ID_NOT_FOUND));

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

        log.info("Sending registration request to EventsService - transactionId: {}, eventId: {}, userId: {}",
                model.getId(), model.getEventId(), model.getUserId());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to register for event. Response status: {}", response.getStatusCode());
            throw new BusinessValidationException(ErrorMessages.FAILED_REGISTRATION);
        }
        log.info("Successfully registered for event. Response: {}", response.getBody());
    }

}
