package com.ems.PaymentsService.entity;

import com.ems.PaymentsService.enums.DBRecordStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_bank_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @Column(name = "user_name", nullable = false, length = 20)
    private String userName;

    @Column(name = "user_account_no", nullable = false, length = 50)
    private String userAccountNo;

    @Column(name = "account_balance", nullable = false)
    private Double accountBalance;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_status")
    private DBRecordStatus recordStatus;

    @Column(name = "last_updated_date", nullable = false)
    private String lastUpdatedDate;

    @Column(name = "last_updated_by", nullable = false, length = 50)
    private String lastUpdatedBy;

}

