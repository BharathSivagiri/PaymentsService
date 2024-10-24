package com.ems.PaymentsService.repositories;

import com.ems.PaymentsService.entity.UserBankAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBankAccountRepository extends JpaRepository<UserBankAccount, Integer>
{

}

