package com.cydeo.service;

import com.cydeo.enums.AccountType;
import com.cydeo.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AccountService {

    Account createAccount(BigDecimal balance, Date createDate, AccountType accountType, Long userId);
    List<Account> listAllAccounts();

}
