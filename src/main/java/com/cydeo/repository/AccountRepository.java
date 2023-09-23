package com.cydeo.repository;

import com.cydeo.exception.RecordNotFoundException;
import com.cydeo.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountRepository {


    public static List<Account> accountList = new ArrayList<>();

    public Account save(Account account){
        accountList.add(account);
        return account;
    }

    public List<Account> findAll(){
        return accountList;
    }


    public Account findById(UUID accountId) {
        //Task
        /* complete the implementation that find the account inside the list, if not
        throw RecordNotFoundException
         */
        return  accountList.stream()
              .filter(account -> account.getAccountId().equals(accountId))
              .findAny()
              .orElseThrow(() -> new RecordNotFoundException("Record doesn't exist in the Database"));

    }


}
