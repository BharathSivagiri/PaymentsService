package com.ems.PaymentsService.utility.constants;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorMessagesTest {

    @Test
    void verifyErrorMessageConstants() {
        assertEquals("Bank account not found", ErrorMessages.BANK_ID_NOT_FOUND);
        assertEquals("Amount paid must be greater than zero", ErrorMessages.AMOUNT_VALIDATION);
        assertEquals("Event ID is required", ErrorMessages.EVENT_ID_NOT_FOUND);
        assertEquals("Payment mode not found", ErrorMessages.PAYMENT_MODE_NOT_FOUND);
        assertEquals("Payment status not found", ErrorMessages.PAYMENT_STATUS_NOT_FOUND);
        assertEquals("Transaction type not found", ErrorMessages.TRANSACTION_TYPE_NOT_FOUND);
        assertEquals("Insufficient balance in bank account", ErrorMessages.INSUFFICIENT_BALANCE);
        assertEquals("No transactions available", ErrorMessages.TRANSACTIONS_NOT_FOUND);
        assertEquals("Record not found", ErrorMessages.RECORD_NOT_FOUND);
        assertEquals("Payment mode not found", ErrorMessages.INVALID_PAYMENT_MODE);
        assertEquals("Payment status not found", ErrorMessages.INVALID_PAYMENT_STATUS);
        assertEquals("Transaction type not found", ErrorMessages.INVALID_TRANSACTION_TYPE);
        assertEquals("Admin account not found", ErrorMessages.ADMIN_ACCOUNT_NOT_FOUND);
        assertEquals("Bank account not found", ErrorMessages.BANK_ACCOUNT_NOT_FOUND);
    }
}
