package com.cydeo.controller;


import com.cydeo.model.Account;
import com.cydeo.model.Transaction;
import com.cydeo.repository.AccountRepository;
import com.cydeo.service.AccountService;
import com.cydeo.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@RequestMapping
@Controller
@AllArgsConstructor
public class TransactionController {

    private  final AccountService accountService;
    private final TransactionService transactionService;



    @GetMapping("/make-transfer")
    public String getMakeTransferPage(Model model){

        //we need to provide empty transaction object
        model.addAttribute("transaction", Transaction.builder().build());

        //we need to provide list of all accounts
        model.addAttribute("accounts",accountService.listAllAccounts());
        //we need list of last 10 transactions to display on the table
        model.addAttribute("latestTransactions", transactionService.last10Transactions());
        return "transaction/make-transfer";
    }


    //write a post method that takes transaction object from the UI
    //complete the transfer and return the same page

    //adding new transaction
    @PostMapping("/transfer-money")
    public String makeTransaction(@ModelAttribute("transaction") Transaction transaction,Model model){

        Account sender = accountService.findAccountById(transaction.getSender());
        Account receiver = accountService.findAccountById(transaction.getReceiver());

        transactionService.makeTransfer(sender,receiver,
                transaction.getTransactionAmount(),
                new Date(),
                transaction.getMessage());

        return "redirect:/make-transfer"; //redirecting above controller method

    }


}
