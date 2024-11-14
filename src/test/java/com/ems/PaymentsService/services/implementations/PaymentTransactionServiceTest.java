//package com.ems.PaymentsService.services.implementations;
//
//import com.ems.PaymentsService.dto.TransactionViewDTO;
//import com.ems.PaymentsService.entity.PaymentTransaction;
//import com.ems.PaymentsService.entity.UserBankAccount;
//import com.ems.PaymentsService.enums.DBRecordStatus;
//import com.ems.PaymentsService.exceptions.custom.BusinessValidationException;
//import com.ems.PaymentsService.exceptions.custom.DataNotFoundException;
//import com.ems.PaymentsService.mapper.PaymentTransactionMapper;
//import com.ems.PaymentsService.model.PaymentTransactionModel;
//import com.ems.PaymentsService.repositories.PaymentTransactionRepository;
//import com.ems.PaymentsService.repositories.UserBankAccountRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PaymentTransactionServiceTest {
//
//    @Mock
//    private PaymentTransactionRepository paymentTransactionRepository;
//
//    @Mock
//    private UserBankAccountRepository userBankAccountRepository;
//
//    @Mock
//    private PaymentTransactionMapper paymentTransactionMapper;
//
//    @InjectMocks
//    private PaymentTransactionService paymentTransactionService;
//
//    private PaymentTransactionModel paymentModel;
//    private PaymentTransaction paymentEntity;
//    private UserBankAccount userAccount;
//    private UserBankAccount adminAccount;
//
//    @BeforeEach
//    void setUp() {
//        paymentModel = new PaymentTransactionModel();
//        paymentModel.setEventId("EVENT123");
//        paymentModel.setAmountPaid("100.00");
//        paymentModel.setPaymentMode("CREDIT_CARD");
//        paymentModel.setTransactionType("DEBIT");
//        paymentModel.setPaymentStatus("PAID");
//        paymentModel.setAccountNumber("ACC123");
//
//        paymentEntity = new PaymentTransaction();
//        paymentEntity.setId(1);
//        paymentEntity.setEventId("EVENT123");
//        paymentEntity.setAmountPaid(100.00);
//
//        userAccount = new UserBankAccount();
//        userAccount.setAccountId(2);
//        userAccount.setAccountBalance(500.00);
//        userAccount.setUserAccountNo("ACC123");
//
//        adminAccount = new UserBankAccount();
//        adminAccount.setAccountId(1);
//        adminAccount.setAccountBalance(1000.00);
//    }
//
//    @Test
//    void createPaymentTransaction_Success() {
//        when(userBankAccountRepository.findByUserAccountNoAndRecordStatus("ACC123", DBRecordStatus.ACTIVE)).thenReturn(Optional.of(userAccount));
//        when(userBankAccountRepository.findById(1)).thenReturn(Optional.of(adminAccount));
//        when(paymentTransactionMapper.toEntity(paymentModel)).thenReturn(paymentEntity);
//        when(paymentTransactionRepository.save(any())).thenReturn(paymentEntity);
//        when(paymentTransactionMapper.toModel(paymentEntity)).thenReturn(paymentModel);
//
//        PaymentTransactionModel result = paymentTransactionService.createPaymentTransaction(paymentModel);
//
//        assertNotNull(result);
//        assertEquals("EVENT123", result.getEventId());
//        verify(userBankAccountRepository, times(2)).save(any());
//        verify(paymentTransactionRepository).save(any());
//    }
//
//    @Test
//    void createRefundTransaction_Success() {
//        paymentModel.setTransactionType("CREDIT");
//
//        when(userBankAccountRepository.findByUserAccountNoAndRecordStatus("ACC123", DBRecordStatus.ACTIVE)).thenReturn(Optional.of(userAccount));
//        when(userBankAccountRepository.findById(1)).thenReturn(Optional.of(adminAccount));
//        when(paymentTransactionMapper.toEntity(paymentModel)).thenReturn(paymentEntity);
//        when(paymentTransactionRepository.save(any())).thenReturn(paymentEntity);
//        when(paymentTransactionMapper.toModel(paymentEntity)).thenReturn(paymentModel);
//
//        PaymentTransactionModel result = paymentTransactionService.createRefundTransaction(paymentModel);
//
//        assertNotNull(result);
//        assertEquals("CREDIT", result.getTransactionType());
//        verify(userBankAccountRepository, times(2)).save(any());
//    }
//
//    @Test
//    void getAllTransactions_Success() {
//        List<PaymentTransaction> transactions = Arrays.asList(paymentEntity);
//        when(paymentTransactionRepository.findAll()).thenReturn(transactions);
//        when(paymentTransactionMapper.toModel(any())).thenReturn(paymentModel);
//
//        List<TransactionViewDTO> result = paymentTransactionService.getAllTransactions();
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void createPaymentTransaction_InsufficientBalance() {
//        paymentModel.setAmountPaid("1000.00");
//        userAccount.setAccountBalance(100.00);
//
//        when(userBankAccountRepository.findByUserAccountNoAndRecordStatus("ACC123", DBRecordStatus.ACTIVE)).thenReturn(Optional.of(userAccount));
//        when(userBankAccountRepository.findById(1)).thenReturn(Optional.of(adminAccount));
//
//        assertThrows(BusinessValidationException.class,
//            () -> paymentTransactionService.createPaymentTransaction(paymentModel));
//    }
//
//    @Test
//    void getAllTransactions_NoTransactionsFound() {
//        when(paymentTransactionRepository.findAll()).thenReturn(Collections.emptyList());
//
//        assertThrows(DataNotFoundException.class,
//            () -> paymentTransactionService.getAllTransactions());
//    }
//}
