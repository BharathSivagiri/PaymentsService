package com.ems.PaymentsService.mapper;

import com.ems.PaymentsService.entity.PaymentTransaction;
import com.ems.PaymentsService.enums.DBRecordStatus;
import com.ems.PaymentsService.enums.PaymentMode;
import com.ems.PaymentsService.enums.PaymentStatus;
import com.ems.PaymentsService.enums.TransactionType;
import com.ems.PaymentsService.model.PaymentTransactionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTransactionMapperTest {

    private PaymentTransactionMapper mapper;
    private PaymentTransactionModel mockModel;
    private PaymentTransaction mockEntity;

    @BeforeEach
    void setUp() {
        mapper = new PaymentTransactionMapper();

        // Setup test model
        mockModel = new PaymentTransactionModel();
        mockModel.setPaymentMode("creditcard");
        mockModel.setAmountPaid("100.50");
        mockModel.setEventId("EVENT123");
        mockModel.setBankId("1001");
        mockModel.setTransactionType("DEBIT");
        mockModel.setPaymentStatus("PAID");

        // Setup test entity
        mockEntity = new PaymentTransaction();
        mockEntity.setPaymentMode(PaymentMode.CREDIT_CARD);
        mockEntity.setAmountPaid(100.50);
        mockEntity.setEventId("EVENT123");
        mockEntity.setBankId(1001);
        mockEntity.setTransactionType(TransactionType.DEBIT);
        mockEntity.setPaymentStatus(PaymentStatus.PAID);
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        PaymentTransaction result = mapper.toEntity(mockModel);

        assertNotNull(result);
        assertEquals(PaymentMode.CREDIT_CARD, result.getPaymentMode());
        assertEquals(100.50, result.getAmountPaid());
        assertEquals("EVENT123", result.getEventId());
        assertEquals(1001, result.getBankId());
        assertEquals(TransactionType.DEBIT, result.getTransactionType());
        assertEquals(PaymentStatus.PAID, result.getPaymentStatus());
        assertEquals(DBRecordStatus.ACTIVE, result.getRecordStatus());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getLastUpdatedDate());
    }

    @Test
    void toEntity_WithNullBankId_ShouldMapCorrectly() {
        mockModel.setBankId(null);
        PaymentTransaction result = mapper.toEntity(mockModel);

        assertNotNull(result);
        assertNull(result.getBankId());
    }

    @Test
    void toModel_ShouldMapAllFieldsCorrectly() {
        PaymentTransactionModel result = mapper.toModel(mockEntity);

        assertNotNull(result);
        assertEquals("CREDIT_CARD", result.getPaymentMode());
        assertEquals("100.5", result.getAmountPaid());
        assertEquals("EVENT123", result.getEventId());
        assertEquals("1001", result.getBankId());
        assertEquals("DEBIT", result.getTransactionType());
        assertEquals("PAID", result.getPaymentStatus());
    }

    @Test
    void toModel_WithNullBankId_ShouldMapCorrectly() {
        mockEntity.setBankId(null);
        PaymentTransactionModel result = mapper.toModel(mockEntity);

        assertNotNull(result);
        assertNull(result.getBankId());
    }
}
