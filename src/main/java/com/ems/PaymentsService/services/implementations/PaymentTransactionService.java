package com.ems.PaymentsService.services.implementations;

import com.ems.PaymentsService.dto.TransactionViewDTO;
import com.ems.PaymentsService.entity.PaymentTransaction;
import com.ems.PaymentsService.entity.UserBankAccount;
import com.ems.PaymentsService.enums.TransactionType;
import com.ems.PaymentsService.mapper.BankAccountBalanceMapper;
import com.ems.PaymentsService.mapper.PaymentTransactionMapper;
import com.ems.PaymentsService.model.PaymentTransactionModel;
import com.ems.PaymentsService.repositories.PaymentTransactionRepository;
import com.ems.PaymentsService.repositories.UserBankAccountRepository;

import com.ems.PaymentsService.validators.BasicPaymentValidator;
import com.ems.PaymentsService.validators.BusinessPaymentValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    BasicPaymentValidator basicValidator;

    @Autowired
    BusinessPaymentValidator businessValidator;

    @Autowired
    BankAccountBalanceMapper bankAccountBalanceMapper;

    @Transactional
    public PaymentTransactionModel createPaymentTransaction(PaymentTransactionModel model) {
        log.info("Creating payment transaction for event ID: {}", model.getEventId());

        basicValidator.validatePaymentTransaction(model);

        UserBankAccount userBankAccount = businessValidator.validateBankAccount(model.getAccountNumber());
        UserBankAccount adminAccount = businessValidator.validateAdminAccount();

        double amount = Double.parseDouble(model.getAmountPaid());
        boolean isDebit = TransactionType.DEBIT.name().equalsIgnoreCase(model.getTransactionType());

        if (isDebit) {
            businessValidator.validateBalance(userBankAccount, amount);
            bankAccountBalanceMapper.updateAdminAccountBalance(adminAccount, amount, true);
            userBankAccountRepository.save(adminAccount);
        }

        PaymentTransaction entity = paymentTransactionMapper.toEntity(model);
        entity = paymentTransactionRepository.save(entity);

        bankAccountBalanceMapper.updateUserAccountBalance(userBankAccount, amount, isDebit);
        userBankAccountRepository.save(userBankAccount);

        return paymentTransactionMapper.toModel(entity);
    }

    @Transactional
    public PaymentTransactionModel createRefundTransaction(PaymentTransactionModel model) {
        log.info("Processing refund for event ID: {}", model.getEventId());

        basicValidator.validatePaymentTransaction(model);

        UserBankAccount userBankAccount = businessValidator.validateBankAccount(model.getAccountNumber());
        UserBankAccount adminAccount = businessValidator.validateAdminAccount();

        paymentTransactionMapper.prepareRefundModel(model, userBankAccount);

        double amount = Double.parseDouble(model.getAmountPaid());
        businessValidator.validateBalance(adminAccount, amount);

        bankAccountBalanceMapper.processRefundBalances(adminAccount, userBankAccount, amount);
        userBankAccountRepository.save(adminAccount);
        userBankAccountRepository.save(userBankAccount);

        PaymentTransaction entity = paymentTransactionMapper.toEntity(model);
        log.info("Refund processed for event ID: {}", model.getEventId());
        return paymentTransactionMapper.toModel(paymentTransactionRepository.save(entity));
    }

    public List<TransactionViewDTO> getAllTransactions() {
        log.info("Retrieving all payment transactions");
        List<PaymentTransaction> transactions = paymentTransactionRepository.findAll();

        businessValidator.validateTransactionsExist(transactions);

        log.info("Found {} transactions", transactions.size());
        return transactions.stream()
                .map(paymentTransactionMapper::toModel)
                .map(TransactionViewDTO::new)
                .collect(Collectors.toList());
    }

}
