package com.ems.PaymentsService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponseDTO {
    private String transactionId;
    private String status;
    private String message;
}

