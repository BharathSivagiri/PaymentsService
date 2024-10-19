package com.ems.PaymentsService.enums;

public enum TransactionType
{
    CREDIT("credit"),
    DEBIT("debit");

    private final String transactionType;

    TransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public static TransactionType fromString(String transactionType) {
        for (TransactionType type : TransactionType.values()) {
            if (type.transactionType.equalsIgnoreCase(transactionType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No constant with text " + transactionType + " found");
    }
}

