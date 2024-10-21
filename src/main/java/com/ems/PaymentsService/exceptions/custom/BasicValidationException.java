package com.ems.PaymentsService.exceptions.custom;

public class BasicValidationException extends RuntimeException
{
    public BasicValidationException(String message)
    {
        super(message);
    }
}
