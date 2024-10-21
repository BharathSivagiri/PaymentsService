package com.ems.PaymentsService.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PaymentTransactionModel {

    private String id;

    @NotBlank(message = "Payment mode is required")
    @Pattern(regexp = "^(upi|creditcard|debitcard)$", message = "Invalid payment mode")
    private String paymentMode;

    @NotBlank(message = "Amount paid is required")
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Invalid amount format")
    private String amountPaid;

    @NotBlank(message = "Event ID is required")
    private String eventId;

    @NotBlank(message = "User ID is required")
    private String userId;

    private String bankId;

    @NotBlank(message = "Transaction type is required")
    @Pattern(regexp = "^(credit|debit)$", message = "Invalid transaction type")
    private String transactionType;

    @NotBlank(message = "Payment status is required")
    @Pattern(regexp = "^(paid|notpaid|paycancelled)$", message = "Invalid payment status")
    private String paymentStatus;

    private String createdBy;
    private String createdDate;
    private String recordStatus;
    private String lastUpdatedDate;
    private String lastUpdatedBy;
}
