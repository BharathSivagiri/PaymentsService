package com.ems.PaymentsService.entity;

import com.ems.PaymentsService.enums.DBRecordStatus;
import com.ems.PaymentsService.enums.PaymentMode;
import com.ems.PaymentsService.enums.PaymentStatus;
import com.ems.PaymentsService.enums.TransactionType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;

    @Column(name = "amount_paid", nullable = false)
    private double amountPaid;

    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "bank_id")
    private Integer bankId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private String createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_status")
    private DBRecordStatus recordStatus;

    @Column(name = "last_updated_date", nullable = false)
    private String lastUpdatedDate;

    @Column(name = "last_updated_by", nullable = false)
    private String lastUpdatedBy;

    @ManyToOne
    @JoinColumn(name = "bank_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserBankAccount userBankAccount;

}
