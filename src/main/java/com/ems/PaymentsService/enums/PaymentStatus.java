package com.ems.PaymentsService.enums;

import com.ems.PaymentsService.utility.constants.ErrorMessages;

public enum PaymentStatus
{
    PAID("paid"),
    NOT_PAID("notpaid"),
    PAY_CANCELLED("paycancelled");

    private final String status;

    PaymentStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public static PaymentStatus fromString(String status)
    {
        for (PaymentStatus paymentStatus : PaymentStatus.values())
        {
            if (paymentStatus.status.equalsIgnoreCase(status))
            {
                return paymentStatus;
            }
        }
        throw new IllegalArgumentException(ErrorMessages.INVALID_PAYMENT_STATUS);
    }
}