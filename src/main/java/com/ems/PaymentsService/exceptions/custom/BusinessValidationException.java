package com.ems.PaymentsService.exceptions.custom;

public class BusinessValidationException extends RuntimeException
{
    public BusinessValidationException(String message) {
        super(message);
    }
}
