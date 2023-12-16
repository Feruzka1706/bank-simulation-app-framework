package com.cydeo.controller;


import com.cydeo.dto.AccountDTO;
import com.cydeo.dto.TransactionDTO;
import com.cydeo.service.AccountService;
import com.cydeo.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RequestMapping
@Controller
@AllArgsConstructor
public class TransactionController {

    private  final AccountService accountService;
    private final TransactionService transactionService;



    @GetMapping("/make-transfer")
    public String getMakeTransferPage(Model model){

        //we need to provide empty transaction object
        model.addAttribute("transactionDTO", new TransactionDTO());

        //we need to provide list of all accounts
        model.addAttribute("accounts",accountService.listAllActiveAccount());
        //we need list of last 10 transactions to display on the table
        model.addAttribute("latestTransactions", transactionService.last10Transactions());
        return "transaction/make-transfer";
    }


    //write a post method that takes transaction object from the UI
    //complete the transfer and return the same page

    //adding new transaction
    @PostMapping("/transfer-money")
    public String makeTransaction(@ModelAttribute("transactionDTO") @Valid TransactionDTO transactionDTO,
                                  BindingResult bindingResult, Model model){
         if(bindingResult.hasErrors()){
              model.addAttribute("accounts",accountService.listAllAccount());
             model.addAttribute("latestTransactions", transactionService.last10Transactions());
             return "transaction/make-transfer";
         }

        //Find the Account objects based on the ID that I have and use as a parameter to complete makeTransfer method
        AccountDTO sender = accountService.findAccountById(transactionDTO.getSender().getId());
        AccountDTO receiver = accountService.findAccountById(transactionDTO.getReceiver().getId());

        transactionService.makeTransfer(sender,receiver,
                transactionDTO.getTransactionAmount(),
                new Date(),
                transactionDTO.getMessage());

        return "redirect:/make-transfer"; //redirecting above controller method

    }

    //write a method that gets the account id from index.html and print on the console
    //work on index.html and here
    //transaction/{id}
    //return transaction/transactions page
    @GetMapping("/transaction/{id}")
    public String getTransactionHistoryPage(@PathVariable("id") Long transactionId, Model model){
        System.out.println(transactionId);

        //get the list of transactions based on id and return as a model attribute
        //Task complete method (service and repo)
        //findTransactionListById
        List<TransactionDTO> transactionDTOList = transactionService.findTransactionListById(transactionId);

        model.addAttribute("transactions", transactionDTOList);

        return "transaction/transactions";
    }

    //go to transactions.html
    //based on size of the transactions either show "No transactions yet" or transactions table

}
