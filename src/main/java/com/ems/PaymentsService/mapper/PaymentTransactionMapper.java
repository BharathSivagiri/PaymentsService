package com.ems.PaymentsService.mapper;

import com.ems.PaymentsService.entity.PaymentTransaction;
import com.ems.PaymentsService.enums.DBRecordStatus;
import com.ems.PaymentsService.enums.PaymentMode;
import com.ems.PaymentsService.enums.PaymentStatus;
import com.ems.PaymentsService.enums.TransactionType;
import com.ems.PaymentsService.model.PaymentTransactionModel;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PaymentTransactionMapper {

    public PaymentTransaction toEntity(PaymentTransactionModel model) {
        PaymentTransaction entity = new PaymentTransaction();

        entity.setPaymentMode(PaymentMode.fromString(model.getPaymentMode()));
        entity.setAmountPaid(Double.parseDouble(model.getAmountPaid()));
        entity.setEventId(model.getEventId());
        entity.setBankId(model.getBankId() != null ? Integer.parseInt(model.getBankId()) : null);
        entity.setTransactionType(TransactionType.fromString(model.getTransactionType()));
        entity.setPaymentStatus(PaymentStatus.fromString(model.getPaymentStatus()));
        entity.setCreatedBy("SYSTEM");
        entity.setCreatedDate(LocalDateTime.now().toString());
        entity.setLastUpdatedBy("SYSTEM");
        entity.setLastUpdatedDate(LocalDateTime.now().toString());
        entity.setRecordStatus(DBRecordStatus.ACTIVE);

        return entity;
    }

    public PaymentTransactionModel toModel(PaymentTransaction entity) {
        PaymentTransactionModel model = new PaymentTransactionModel();

        model.setPaymentMode(entity.getPaymentMode().name());
        model.setAmountPaid(String.valueOf(entity.getAmountPaid()));
        model.setEventId(entity.getEventId());
        model.setBankId(entity.getBankId() != null ? String.valueOf(entity.getBankId()) : null);
        model.setTransactionType(entity.getTransactionType().name());
        model.setPaymentStatus(entity.getPaymentStatus().name());

        return model;
    }

    public PaymentTransaction toRefundEntity(PaymentTransaction originalTransaction) {
        PaymentTransaction refundTransaction = new PaymentTransaction();

        refundTransaction.setEventId(originalTransaction.getEventId());
        refundTransaction.setAmountPaid(originalTransaction.getAmountPaid());
        refundTransaction.setPaymentMode(originalTransaction.getPaymentMode());
        refundTransaction.setTransactionType(TransactionType.CREDIT);
        refundTransaction.setPaymentStatus(PaymentStatus.PAID);
        refundTransaction.setBankId(originalTransaction.getBankId());
        refundTransaction.setCreatedBy("SYSTEM");
        refundTransaction.setCreatedDate(LocalDateTime.now().toString());
        refundTransaction.setLastUpdatedBy("SYSTEM");
        refundTransaction.setLastUpdatedDate(LocalDateTime.now().toString());
        refundTransaction.setRecordStatus(DBRecordStatus.ACTIVE);

        return refundTransaction;
    }

}
