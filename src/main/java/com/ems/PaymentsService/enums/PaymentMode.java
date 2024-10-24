package com.ems.PaymentsService.enums;

import com.ems.PaymentsService.utility.constants.ErrorMessages;

public enum PaymentMode
{
    UPI("upi"),
    CREDIT_CARD("creditcard"),
    DEBIT_CARD("debitcard");

    private final String mode;

    PaymentMode(String mode)
    {
        this.mode = mode;
    }

    public String getMode()
    {
        return mode;
    }

    public static PaymentMode fromString(String mode)
    {
        for (PaymentMode paymentMode : PaymentMode.values())
        {
            if (paymentMode.mode.equalsIgnoreCase(mode))
            {
                return paymentMode;
            }
        }
        throw new IllegalArgumentException(ErrorMessages.INVALID_PAYMENT_MODE);
    }
}

