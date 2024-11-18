package com.ems.PaymentsService.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTransactionModelTest {

    private Validator validator;
    private PaymentTransactionModel model;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        model = new PaymentTransactionModel();
    }

    @Test
    void testValidPaymentTransaction() {
        model.setPaymentMode("creditcard");
        model.setAmountPaid("100.00");
        model.setEventId("EVENT123");
        model.setUserId("USER123");
        model.setTransactionType("debit");
        model.setPaymentStatus("paid");

        var violations = validator.validate(model);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidPaymentMode() {
        model.setPaymentMode("cash");
        model.setAmountPaid("100.00");
        model.setEventId("EVENT123");
        model.setUserId("USER123");
        model.setTransactionType("debit");
        model.setPaymentStatus("paid");

        var violations = validator.validate(model);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Invalid payment mode")));
    }

    @Test
    void testInvalidAmountFormat() {
        model.setPaymentMode("creditcard");
        model.setAmountPaid("100.000");
        model.setEventId("EVENT123");
        model.setUserId("USER123");
        model.setTransactionType("debit");
        model.setPaymentStatus("paid");

        var violations = validator.validate(model);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Invalid amount format")));
    }

    @Test
    void testInvalidTransactionType() {
        model.setPaymentMode("creditcard");
        model.setAmountPaid("100.00");
        model.setEventId("EVENT123");
        model.setUserId("USER123");
        model.setTransactionType("invalid");
        model.setPaymentStatus("paid");

        var violations = validator.validate(model);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Invalid transaction type")));
    }

    @Test
    void testInvalidPaymentStatus() {
        model.setPaymentMode("creditcard");
        model.setAmountPaid("100.00");
        model.setEventId("EVENT123");
        model.setUserId("USER123");
        model.setTransactionType("debit");
        model.setPaymentStatus("invalid");

        var violations = validator.validate(model);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Invalid payment status")));
    }

    @Test
    void testMissingRequiredFields() {
        var violations = validator.validate(model);
        assertFalse(violations.isEmpty());
        assertEquals(6, violations.size());
    }

    @Test
    void testOptionalFields() {
        model.setPaymentMode("creditcard");
        model.setAmountPaid("100.00");
        model.setEventId("EVENT123");
        model.setUserId("USER123");
        model.setTransactionType("debit");
        model.setPaymentStatus("paid");
        model.setAccountNumber("ACC123");
        model.setBankId("BANK123");
        model.setCreatedBy("SYSTEM");
        model.setCreatedDate("2023-01-01");
        model.setRecordStatus("ACTIVE");
        model.setLastUpdatedDate("2023-01-01");
        model.setLastUpdatedBy("SYSTEM");

        var violations = validator.validate(model);
        assertTrue(violations.isEmpty());
    }
}
