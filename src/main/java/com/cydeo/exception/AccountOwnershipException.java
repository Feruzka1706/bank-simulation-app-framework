package com.cydeo.exception;

import com.cydeo.model.Account;

public class AccountOwnershipException extends RuntimeException{

    public AccountOwnershipException(String message){
        super(message);
    }
}
