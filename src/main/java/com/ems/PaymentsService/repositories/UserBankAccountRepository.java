package com.ems.PaymentsService.repositories;

import com.ems.PaymentsService.entity.UserBankAccount;

import com.ems.PaymentsService.enums.DBRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBankAccountRepository extends JpaRepository<UserBankAccount, Integer>
{
    Optional<UserBankAccount> findByUserAccountNoAndRecordStatus(String accountNumber, DBRecordStatus recordStatus);

    Optional<UserBankAccount> findByAccountIdAndRecordStatus(Integer accountId, DBRecordStatus recordStatus);
}

