package com.cydeo.repository;

import com.cydeo.dto.AccountDTO;
import com.cydeo.entity.Account;
import com.cydeo.enums.AccountStatus;
import com.cydeo.exception.RecordNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

//    @Query("select a from Account a where a.accountStatus = 'ACTIVE'")
//    List<Account> getAllActiveAccounts();

    List<Account> findAllByAccountStatus(AccountStatus accountStatus);


}
