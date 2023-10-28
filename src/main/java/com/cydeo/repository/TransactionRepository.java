package com.cydeo.repository;

import com.cydeo.model.Account;
import com.cydeo.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Repository
public class TransactionRepository {

    public static List<Transaction> transactionList = new ArrayList<>();

    public Transaction save(Transaction transaction){
        transactionList.add(transaction);
        return transaction;
    }

    public List<Transaction> findAll() {
        return transactionList;
    }


    public List<Transaction> findByTransactionId(UUID transactionId){
        return transactionList.stream()
                .filter(transaction -> transaction.getSender().equals(transactionId)
                        || transaction.getReceiver().equals(transactionId))
                .collect(Collectors.toList());
    }
}
