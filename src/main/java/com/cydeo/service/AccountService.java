package com.cydeo.service;

import com.cydeo.enums.AccountType;
import com.cydeo.dto.AccountDTO;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AccountService {

    void createNewAccount(AccountDTO accountDTO);
    List<AccountDTO> listAllAccounts();

    void deleteAccountById(Long accountId) ;

    void activateDeletedAccountById(Long accountId);

    AccountDTO findAccountById(Long accountId);

    List<AccountDTO> listAllActiveAccount();


    void updateAccount(AccountDTO accountDTO);
}
