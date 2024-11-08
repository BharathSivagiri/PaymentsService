package com.ems.PaymentsService.entity;

import com.ems.PaymentsService.enums.DBRecordStatus;
import com.ems.PaymentsService.enums.PaymentMode;
import com.ems.PaymentsService.enums.PaymentStatus;
import com.ems.PaymentsService.enums.TransactionType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTransactionTest {

    @Test
    void testPaymentTransactionCreation() {
        PaymentTransaction transaction = new PaymentTransaction();
        assertNotNull(transaction);
    }

    @Test
    void testPaymentTransactionSettersAndGetters() {
        PaymentTransaction transaction = new PaymentTransaction();

        transaction.setId(1);
        transaction.setPaymentMode(PaymentMode.CREDIT_CARD);
        transaction.setAmountPaid(100.50);
        transaction.setEventId("EVENT123");
        transaction.setBankId(1001);
        transaction.setTransactionType(TransactionType.DEBIT);
        transaction.setPaymentStatus(PaymentStatus.PAID);
        transaction.setCreatedBy("user1");
        transaction.setCreatedDate("2024-01-20");
        transaction.setRecordStatus(DBRecordStatus.ACTIVE);
        transaction.setLastUpdatedDate("2024-01-20");
        transaction.setLastUpdatedBy("user1");

        assertEquals(1, transaction.getId());
        assertEquals(PaymentMode.CREDIT_CARD, transaction.getPaymentMode());
        assertEquals(100.50, transaction.getAmountPaid());
        assertEquals("EVENT123", transaction.getEventId());
        assertEquals(1001, transaction.getBankId());
        assertEquals(TransactionType.DEBIT, transaction.getTransactionType());
        assertEquals(PaymentStatus.PAID, transaction.getPaymentStatus());
        assertEquals("user1", transaction.getCreatedBy());
        assertEquals("2024-01-20", transaction.getCreatedDate());
        assertEquals(DBRecordStatus.ACTIVE, transaction.getRecordStatus());
        assertEquals("2024-01-20", transaction.getLastUpdatedDate());
        assertEquals("user1", transaction.getLastUpdatedBy());
    }

    @Test
    void testAllArgsConstructor() {
        PaymentTransaction transaction = new PaymentTransaction(
            1,
            PaymentMode.CREDIT_CARD,
            100.50,
            "EVENT123",
            1001,
            TransactionType.DEBIT,
            PaymentStatus.PAID,
            "user1",
            "2024-01-20",
            DBRecordStatus.ACTIVE,
            "2024-01-20",
            "user1",
            null
        );

        assertNotNull(transaction);
        assertEquals(1, transaction.getId());
        assertEquals(PaymentMode.CREDIT_CARD, transaction.getPaymentMode());
        assertEquals(100.50, transaction.getAmountPaid());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentTransaction transaction1 = new PaymentTransaction();
        transaction1.setId(1);
        transaction1.setEventId("EVENT123");

        PaymentTransaction transaction2 = new PaymentTransaction();
        transaction2.setId(1);
        transaction2.setEventId("EVENT123");

        assertEquals(transaction1, transaction2);
        assertEquals(transaction1.hashCode(), transaction2.hashCode());
    }
}
