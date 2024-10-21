package com.ems.PaymentsService.exceptions.custom;

public class DataNotFoundException extends RuntimeException
{
    public DataNotFoundException(String message)
    {
        super(message);
    }
}
