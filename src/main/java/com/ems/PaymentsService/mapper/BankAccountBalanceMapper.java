package com.ems.PaymentsService.mapper;

import com.ems.PaymentsService.entity.UserBankAccount;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BankAccountBalanceMapper {

    public void updateAdminAccountBalance(UserBankAccount adminAccount, double amount, boolean isDebit) {
        if(isDebit) {
            adminAccount.setAccountBalance(adminAccount.getAccountBalance() + amount);
        } else {
            adminAccount.setAccountBalance(adminAccount.getAccountBalance() - amount);
        }
        adminAccount.setLastUpdatedDate(String.valueOf(LocalDateTime.now()));
    }

    public void updateUserAccountBalance(UserBankAccount userAccount, double amount, boolean isDebit) {
        if(isDebit) {
            userAccount.setAccountBalance(userAccount.getAccountBalance() - amount);
        } else {
            userAccount.setAccountBalance(userAccount.getAccountBalance() + amount);
        }
        userAccount.setLastUpdatedDate(String.valueOf(LocalDateTime.now()));
    }

    public void processRefundBalances(UserBankAccount adminAccount, UserBankAccount userAccount, double amount) {
        // Deduct from admin account
        adminAccount.setAccountBalance(adminAccount.getAccountBalance() - amount);

        // Add to user account
        userAccount.setAccountBalance(userAccount.getAccountBalance() + amount);
    }

}
