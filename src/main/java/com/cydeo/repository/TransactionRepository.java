package com.cydeo.repository;

import com.cydeo.dto.TransactionDTO;
import com.cydeo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    //List<Transaction> findByTransactionId(Long transactionId);
    @Query("select t from Transaction t where t.sender= ?1 " +
            "or t.receiver=?1")
    List<Transaction> findAllTransactionsByAccountId(Long transactionId);

    List<Transaction> findTop10ByOrderByTransactionDateDesc();


    //Write Native query to retrieve latest 10 last created transactions
    @Query(value = "select * from transactions order by transaction_date desc limit 10",nativeQuery = true)
    List<Transaction> findLast10Transactions();


}
