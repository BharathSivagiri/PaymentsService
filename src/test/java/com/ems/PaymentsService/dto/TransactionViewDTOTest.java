package com.ems.PaymentsService.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.ems.PaymentsService.model.PaymentTransactionModel;

class TransactionViewDTOTest {

    @Test
    void testNoArgsConstructor() {
        TransactionViewDTO dto = new TransactionViewDTO();
        assertNotNull(dto);
    }

    @Test
    void testParameterizedConstructor() {
        PaymentTransactionModel model = new PaymentTransactionModel();
        model.setPaymentMode("CREDIT_CARD");
        model.setAmountPaid("1000");
        model.setEventId("EVENT123");
        model.setBankId("BANK456");
        model.setTransactionType("CREDIT");
        model.setPaymentStatus("PAID");

        TransactionViewDTO dto = new TransactionViewDTO(model);

        assertEquals("CREDIT_CARD", dto.getPaymentMode());
        assertEquals("1000", dto.getAmountPaid());
        assertEquals("EVENT123", dto.getEventId());
        assertEquals("BANK456", dto.getBankId());
        assertEquals("CREDIT", dto.getTransactionType());
        assertEquals("PAID", dto.getPaymentStatus());
    }

    @Test
    void testSettersAndGetters() {
        TransactionViewDTO dto = new TransactionViewDTO();

        dto.setPaymentMode("DEBIT_CARD");
        dto.setAmountPaid("500");
        dto.setEventId("EVENT789");
        dto.setBankId("BANK123");
        dto.setTransactionType("DEBIT");
        dto.setPaymentStatus("PAY_CANCELLED");

        assertEquals("DEBIT_CARD", dto.getPaymentMode());
        assertEquals("500", dto.getAmountPaid());
        assertEquals("EVENT789", dto.getEventId());
        assertEquals("BANK123", dto.getBankId());
        assertEquals("DEBIT", dto.getTransactionType());
        assertEquals("PAY_CANCELLED", dto.getPaymentStatus());
    }
}
