package com.ems.PaymentsService.exceptions.custom;

public class DateInvalidException extends RuntimeException
{
    public DateInvalidException(String message) {
        super(message);
    }
}
