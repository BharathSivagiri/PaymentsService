package com.ems.PaymentsService.validators;

import com.ems.PaymentsService.exceptions.custom.BasicValidationException;
import com.ems.PaymentsService.model.PaymentTransactionModel;
import com.ems.PaymentsService.utility.constants.ErrorMessages;
import org.springframework.stereotype.Component;

@Component
public class BasicPaymentValidator {
    public void validatePaymentTransaction(PaymentTransactionModel model) {
        if (model.getAmountPaid() == null || Double.parseDouble(model.getAmountPaid()) <= 0) {
            throw new BasicValidationException(ErrorMessages.AMOUNT_VALIDATION);
        }
        if (model.getEventId() == null || model.getEventId().trim().isEmpty()) {
            throw new BasicValidationException(ErrorMessages.EVENT_ID_NOT_FOUND);
        }
        if (model.getPaymentMode() == null) {
            throw new BasicValidationException(ErrorMessages.PAYMENT_MODE_NOT_FOUND);
        }
        if (model.getTransactionType() == null) {
            throw new BasicValidationException(ErrorMessages.TRANSACTION_TYPE_NOT_FOUND);
        }
        if (model.getPaymentStatus() == null) {
            throw new BasicValidationException(ErrorMessages.PAYMENT_STATUS_NOT_FOUND);
        }
    }
}
