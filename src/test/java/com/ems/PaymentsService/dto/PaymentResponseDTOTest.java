package com.ems.PaymentsService.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentResponseDTOTest {

    @Test
    void testAllArgsConstructor() {
        PaymentResponseDTO response = new PaymentResponseDTO("TXN123", "SUCCESS", "Payment processed successfully");

        assertEquals("TXN123", response.getTransactionId());
        assertEquals("SUCCESS", response.getStatus());
        assertEquals("Payment processed successfully", response.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        PaymentResponseDTO response = new PaymentResponseDTO("TXN123", "SUCCESS", "Payment processed successfully");

        response.setTransactionId("TXN456");
        response.setStatus("FAILED");
        response.setMessage("Payment failed");

        assertEquals("TXN456", response.getTransactionId());
        assertEquals("FAILED", response.getStatus());
        assertEquals("Payment failed", response.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        PaymentResponseDTO response1 = new PaymentResponseDTO("TXN123", "SUCCESS", "Payment processed successfully");
        PaymentResponseDTO response2 = new PaymentResponseDTO("TXN123", "SUCCESS", "Payment processed successfully");
        PaymentResponseDTO response3 = new PaymentResponseDTO("TXN456", "FAILED", "Payment failed");

        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void testToString() {
        PaymentResponseDTO response = new PaymentResponseDTO("TXN123", "SUCCESS", "Payment processed successfully");
        String toString = response.toString();

        assertTrue(toString.contains("TXN123"));
        assertTrue(toString.contains("SUCCESS"));
        assertTrue(toString.contains("Payment processed successfully"));
    }
}
