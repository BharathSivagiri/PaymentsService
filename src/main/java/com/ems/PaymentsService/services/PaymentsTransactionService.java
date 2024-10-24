package com.ems.PaymentsService.services;

import com.ems.PaymentsService.model.PaymentTransactionModel;

public interface PaymentsTransactionService
{
    boolean hasExistingTransaction(String eventId, String userId);
    void creditBackAmount(PaymentTransactionModel transaction);
    void updateRegistrationStatus(String eventId, String userId, String status);
}
