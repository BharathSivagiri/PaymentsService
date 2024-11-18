package com.ems.PaymentsService.entity;

import com.ems.PaymentsService.enums.DBRecordStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserBankAccountTest {

    @Test
    void testNoArgsConstructor() {
        UserBankAccount userBankAccount = new UserBankAccount();
        assertNotNull(userBankAccount);
    }

    @Test
    void testAllArgsConstructor() {
        UserBankAccount userBankAccount = new UserBankAccount(
            1,
            "testUser",
            "ACC123456789",
            1000.0,
            "admin",
            "2024-01-20",
            DBRecordStatus.ACTIVE,
            "2024-01-20",
            "admin"
        );

        assertEquals(1, userBankAccount.getAccountId());
        assertEquals("testUser", userBankAccount.getUserName());
        assertEquals("ACC123456789", userBankAccount.getUserAccountNo());
        assertEquals(1000.0, userBankAccount.getAccountBalance());
        assertEquals("admin", userBankAccount.getCreatedBy());
        assertEquals("2024-01-20", userBankAccount.getCreatedDate());
        assertEquals(DBRecordStatus.ACTIVE, userBankAccount.getRecordStatus());
        assertEquals("2024-01-20", userBankAccount.getLastUpdatedDate());
        assertEquals("admin", userBankAccount.getLastUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        UserBankAccount userBankAccount = new UserBankAccount();

        userBankAccount.setAccountId(2);
        userBankAccount.setUserName("johnDoe");
        userBankAccount.setUserAccountNo("ACC987654321");
        userBankAccount.setAccountBalance(2000.0);
        userBankAccount.setCreatedBy("system");
        userBankAccount.setCreatedDate("2024-01-21");
        userBankAccount.setRecordStatus(DBRecordStatus.INACTIVE);
        userBankAccount.setLastUpdatedDate("2024-01-21");
        userBankAccount.setLastUpdatedBy("system");

        assertEquals(2, userBankAccount.getAccountId());
        assertEquals("johnDoe", userBankAccount.getUserName());
        assertEquals("ACC987654321", userBankAccount.getUserAccountNo());
        assertEquals(2000.0, userBankAccount.getAccountBalance());
        assertEquals("system", userBankAccount.getCreatedBy());
        assertEquals("2024-01-21", userBankAccount.getCreatedDate());
        assertEquals(DBRecordStatus.INACTIVE, userBankAccount.getRecordStatus());
        assertEquals("2024-01-21", userBankAccount.getLastUpdatedDate());
        assertEquals("system", userBankAccount.getLastUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        UserBankAccount account1 = new UserBankAccount(1, "user1", "ACC111", 1000.0, "admin", "2024-01-20", DBRecordStatus.ACTIVE, "2024-01-20", "admin");
        UserBankAccount account2 = new UserBankAccount(1, "user1", "ACC111", 1000.0, "admin", "2024-01-20", DBRecordStatus.ACTIVE, "2024-01-20", "admin");
        UserBankAccount account3 = new UserBankAccount(2, "user2", "ACC222", 2000.0, "system", "2024-01-21", DBRecordStatus.INACTIVE, "2024-01-21", "system");

        assertTrue(account1.equals(account2));
        assertEquals(account1.hashCode(), account2.hashCode());
        assertFalse(account1.equals(account3));
        assertNotEquals(account1.hashCode(), account3.hashCode());
    }
}
