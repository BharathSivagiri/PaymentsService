package com.ems.PaymentsService.validators;

import com.ems.PaymentsService.entity.PaymentTransaction;
import com.ems.PaymentsService.entity.UserBankAccount;
import com.ems.PaymentsService.enums.DBRecordStatus;
import com.ems.PaymentsService.exceptions.custom.BusinessValidationException;
import com.ems.PaymentsService.repositories.UserBankAccountRepository;
import com.ems.PaymentsService.utility.constants.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class BusinessPaymentValidator {
    @Autowired
    UserBankAccountRepository userBankAccountRepository;

    public UserBankAccount validateBankAccount(String accountNumber) {
        return userBankAccountRepository.findByUserAccountNoAndRecStatus(accountNumber, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BusinessValidationException(ErrorMessages.BANK_ACCOUNT_NOT_FOUND));
    }

    public UserBankAccount validateAdminAccount() {
        return userBankAccountRepository.findByAccountIdAndRecStatus(1, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BusinessValidationException(ErrorMessages.ADMIN_ACCOUNT_NOT_FOUND));
    }

    public void validateBalance(UserBankAccount account, double amount) {
        if (account.getAccountBalance() < amount) {
            throw new BusinessValidationException(ErrorMessages.INSUFFICIENT_BALANCE);
        }
    }

    public void validateTransactionsExist(List<PaymentTransaction> transactions) {
        if (transactions.isEmpty()) {
            log.error("No transactions found in the system");
            throw new BusinessValidationException(ErrorMessages.TRANSACTIONS_NOT_FOUND);
        }
    }

}
