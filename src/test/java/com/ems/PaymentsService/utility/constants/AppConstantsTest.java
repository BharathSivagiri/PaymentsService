package com.ems.PaymentsService.utility.constants;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppConstantsTest {

    @Test
    void testDateFormatConstant() {
        assertEquals("yyyyMMdd", AppConstants.DATE_FORMAT);
    }

    @Test
    void testSystemUserConstant() {
        assertEquals("System", AppConstants.SYSTEM_USER);
    }

    @Test
    void testConstantsAreNotNull() {
        assertNotNull(AppConstants.DATE_FORMAT);
        assertNotNull(AppConstants.SYSTEM_USER);
    }

    @Test
    void testConstantsHaveCorrectType() {
        assertTrue(AppConstants.DATE_FORMAT instanceof String);
        assertTrue(AppConstants.SYSTEM_USER instanceof String);
    }
}
