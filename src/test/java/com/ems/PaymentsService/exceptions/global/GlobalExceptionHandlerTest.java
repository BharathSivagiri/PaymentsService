package com.ems.PaymentsService.exceptions.global;

import com.ems.PaymentsService.exceptions.custom.BusinessValidationException;
import com.ems.PaymentsService.exceptions.custom.PaymentProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleBusinessValidationException_ShouldReturnBadRequest() {
        String errorMessage = "Business validation failed";
        BusinessValidationException exception = new BusinessValidationException(errorMessage);

        ResponseEntity<String> response = globalExceptionHandler.handleBusinessValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
    }

    @Test
    void handleValidationExceptions_ShouldReturnBadRequest() {
        MethodArgumentNotValidException mockException = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError = new FieldError("objectName", "fieldName", "Field validation failed");
        when(mockException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(java.util.Collections.singletonList(fieldError));

        ResponseEntity<String> response = globalExceptionHandler.handleValidationExceptions(mockException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Field validation failed", response.getBody());
    }

    @Test
    void handlePaymentProcessingException_ShouldReturnBadRequest() {
        String errorMessage = "Payment processing failed";
        PaymentProcessingException exception = new PaymentProcessingException(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlePaymentProcessingException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
