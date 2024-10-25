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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PaymentTransactionService
{
    @Autowired
    PaymentTransactionRepository paymentTransactionRepository;

    @Autowired
    UserBankAccountRepository userBankAccountRepository;

    @Autowired
    PaymentTransactionMapper paymentTransactionMapper;

    @Transactional
    public PaymentTransactionModel createPaymentTransaction(PaymentTransactionModel model) {
        log.info("Creating payment transaction for event ID: {}", model.getEventId());
        validatePaymentTransaction(model);

        UserBankAccount userBankAccount = userBankAccountRepository.findById(Integer.parseInt(model.getBankId()))
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.BANK_ID_NOT_FOUND));

        UserBankAccount adminAccount = userBankAccountRepository.findById(1)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.ADMIN_ACCOUNT_NOT_FOUND));

        if (TransactionType.DEBIT.name().equalsIgnoreCase(model.getTransactionType())) {
            double amount = Double.parseDouble(model.getAmountPaid());
            if (userBankAccount.getAccountBalance() < amount) {
                throw new BusinessValidationException(ErrorMessages.INSUFFICIENT_BALANCE);
            }
            adminAccount.setAccountBalance(adminAccount.getAccountBalance() + amount);
            userBankAccountRepository.save(adminAccount);
        }

        PaymentTransaction entity = paymentTransactionMapper.toEntity(model);
        entity = paymentTransactionRepository.save(entity);

        updateBankAccountBalance(userBankAccount, model);

        return paymentTransactionMapper.toModel(entity);
    }


    private void validatePaymentTransaction(PaymentTransactionModel model)
    {
        log.debug("Validating payment transaction");

        if (model.getAmountPaid() == null || Double.parseDouble(model.getAmountPaid()) <= 0)
        {
            throw new BusinessValidationException(ErrorMessages.AMOUNT_VALIDATION);
        }

        if (model.getEventId() == null || model.getEventId().trim().isEmpty())
        {
            throw new BusinessValidationException(ErrorMessages.EVENT_ID_NOT_FOUND);
        }

        if (model.getPaymentMode() == null)
        {
            throw new BusinessValidationException(ErrorMessages.PAYMENT_MODE_NOT_FOUND);
        }

        if (model.getTransactionType() == null)
        {
            throw new BusinessValidationException(ErrorMessages.TRANSACTION_TYPE_NOT_FOUND);
        }


        if (model.getPaymentStatus() == null)
        {
            throw new BusinessValidationException(ErrorMessages.PAYMENT_STATUS_NOT_FOUND);
        }

        log.debug("Payment transaction validation completed");
    }

    private void updateBankAccountBalance(UserBankAccount account, PaymentTransactionModel model)
    {
        double amount = Double.parseDouble(model.getAmountPaid());
        if (TransactionType.DEBIT.name().equalsIgnoreCase(model.getTransactionType()))
        {
            if (account.getAccountBalance() < amount)
            {
                throw new BusinessValidationException(ErrorMessages.INSUFFICIENT_BALANCE);
            }
            account.setAccountBalance(account.getAccountBalance() - amount);
        }
        else
        {
            account.setAccountBalance(account.getAccountBalance() + amount);
        }
        account.setLastUpdatedDate(String.valueOf(LocalDateTime.now()));
        userBankAccountRepository.save(account);
        log.info("Bank account balance updated for account ID: {}", account.getAccountId());
    }

    public List<TransactionViewDTO> getAllTransactions()
    {
        log.info("Retrieving all payment transactions");
        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();

        if (transactions.isEmpty())
        {
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
