package com.ems.PaymentsService.controller;

import com.ems.PaymentsService.dto.TransactionViewDTO;
import com.ems.PaymentsService.enums.TransactionType;
import com.ems.PaymentsService.model.PaymentTransactionModel;
import com.ems.PaymentsService.entity.PaymentTransaction;
import com.ems.PaymentsService.repositories.PaymentTransactionRepository;
import com.ems.PaymentsService.services.implementations.PaymentTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PaymentTransactionControllerTest {

    @Mock
    private PaymentTransactionService paymentTransactionService;

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @InjectMocks
    private PaymentTransactionController paymentTransactionController;

    private PaymentTransactionModel mockPaymentTransaction;
    private PaymentTransaction mockPaymentEntity;
    private TransactionViewDTO mockTransactionViewDTO;

    @BeforeEach
    void setUp() {
        mockPaymentTransaction = new PaymentTransactionModel();
        mockPaymentTransaction.setId("1");
        mockPaymentTransaction.setTransactionType("DEBIT");
        mockPaymentTransaction.setAmountPaid("100.0");

        mockPaymentEntity = new PaymentTransaction();
        mockPaymentEntity.setId(1);
        mockPaymentEntity.setTransactionType(TransactionType.valueOf("DEBIT"));
        mockPaymentEntity.setAmountPaid(100.0);

        mockTransactionViewDTO = new TransactionViewDTO();
        mockTransactionViewDTO.setAmountPaid("100.0");
    }

    @Test
    void processPayment_ShouldReturnTransactionId() {
        when(paymentTransactionService.createPaymentTransaction(any()))
                .thenReturn(mockPaymentTransaction);
        when(paymentTransactionRepository.findFirstByOrderByIdDesc())
                .thenReturn(mockPaymentEntity);

        ResponseEntity<Integer> response = paymentTransactionController.processPayment(mockPaymentTransaction);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    @Test
    void processRefund_ShouldReturnTransactionId() {
        mockPaymentTransaction.setTransactionType("CREDIT");
        mockPaymentEntity.setTransactionType(TransactionType.valueOf("CREDIT"));

        when(paymentTransactionService.createRefundTransaction(any()))
                .thenReturn(mockPaymentTransaction);
        when(paymentTransactionRepository.findFirstByOrderByIdDesc())
                .thenReturn(mockPaymentEntity);

        ResponseEntity<Integer> response = paymentTransactionController.processRefund(mockPaymentTransaction);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
    }

    @Test
    void getAllTransactions_ShouldReturnListOfTransactions() {
        List<TransactionViewDTO> mockTransactions = Arrays.asList(mockTransactionViewDTO);
        when(paymentTransactionService.getAllTransactions()).thenReturn(mockTransactions);

        ResponseEntity<List<TransactionViewDTO>> response = paymentTransactionController.getAllTransactions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
