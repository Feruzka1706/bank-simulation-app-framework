package com.cydeo.service.impl;

import com.cydeo.entity.Account;
import com.cydeo.enums.AccountStatus;
import com.cydeo.dto.AccountDTO;
import com.cydeo.mapper.AccountMapper;
import com.cydeo.repository.AccountRepository;
import com.cydeo.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

   private  final AccountRepository accountRepository;
   private  final AccountMapper accountMapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
    }

    @Override
    public void  createNewAccount(AccountDTO accountDTO) {
        if (accountDTO == null) {
            throw new IllegalArgumentException("accountDTO must not be null");
        }

        if (accountMapper == null) {
            throw new IllegalStateException("accountMapper is not initialized");
        }

        if (accountRepository == null) {
            throw new IllegalStateException("accountRepository is not initialized");
        }

        accountDTO.setCreationDate(new Date());
        accountDTO.setAccountStatus(AccountStatus.ACTIVE);
        //save into the database(repository)
        accountRepository.save(accountMapper.convertToEntity(accountDTO));
    }

    @Override
    public List<AccountDTO> listAllAccount() {
        //we are getting list of account but we need to return list of AccountDTO
        List<Account> accountList = accountRepository.findAll();
        //we are converting entity to dto list and return it
        return accountList.stream().map(accountMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteAccountById(Long accountId) {
        //find the account belongs the id
        //set the status of account as Deleted
        // Find the account by ID
        Optional<Account> optionalAccount = accountRepository.findById(accountId);


        // Check if the account exists
        if (optionalAccount.isPresent()) {
            // Get the account entity
          Account accountEntity = optionalAccount.get();

          // Set the status of the account as DELETED
          accountEntity.setAccountStatus(AccountStatus.DELETED);

          // Save the updated account entity to the database
          accountRepository.save(accountEntity);
        }
    }


    @Override
    public void activateDeletedAccountById (Long accountId){
       // Find the account by ID
       Optional<Account> optionalAccount = accountRepository.findById(accountId);

        // Check if the account exists
         if (optionalAccount.isPresent() && optionalAccount.get().getAccountStatus() == AccountStatus.DELETED) {
          // Get the account entity
          Account accountEntity = optionalAccount.get();

          // Set the status of the account as ACTIVE
          accountEntity.setAccountStatus(AccountStatus.ACTIVE);

          // Save the updated account entity to the database
          accountRepository.save(accountEntity);
         }

    }



    @Override
    public AccountDTO findAccountById (Long accountId) {
       return accountMapper.convertToDTO(accountRepository.findById(accountId).get());
    }

    /**
    @Override
    public List<AccountDTO> listAllActiveAccount() {
        return listAllAccounts().stream()
                .filter(accountDTO -> accountDTO.getAccountStatus().equals(AccountStatus.ACTIVE))
                .collect(Collectors.toList());
    }
    */

    @Override
    public List<AccountDTO> listAllActiveAccount() {

        //we need list of active accounts from DB
        //then convert active accounts to account DTO
        return accountRepository.findAllByAccountStatus(AccountStatus.ACTIVE)
                .stream()
                .map(accountMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateAccount(AccountDTO accountDTO) {

        // Save the updated entity back to the database
        accountRepository.save(accountMapper.convertToEntity(accountDTO));
    }


}
