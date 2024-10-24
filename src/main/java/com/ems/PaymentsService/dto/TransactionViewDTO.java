package com.ems.PaymentsService.dto;

import com.ems.PaymentsService.model.PaymentTransactionModel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionViewDTO {
    private String paymentMode;
    private String amountPaid;
    private String eventId;
    private String bankId;
    private String transactionType;
    private String paymentStatus;

    public TransactionViewDTO(PaymentTransactionModel model) {
        this.paymentMode = model.getPaymentMode();
        this.amountPaid = model.getAmountPaid();
        this.eventId = model.getEventId();
        this.bankId = model.getBankId();
        this.transactionType = model.getTransactionType();
        this.paymentStatus = model.getPaymentStatus();
    }
}
