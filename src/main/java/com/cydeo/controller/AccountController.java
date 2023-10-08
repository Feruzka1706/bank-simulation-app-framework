package com.cydeo.controller;

import com.cydeo.enums.AccountType;
import com.cydeo.model.Account;
import com.cydeo.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.UUID;

@Controller
@AllArgsConstructor
//@RequestMapping we don't have to explicitly mention RequestMapping if we are using @GetMapping in method level
public class AccountController {

     AccountService accountService;


    /*
    write a method to return index.html page including account list information
    endpoint:index
     */
    @GetMapping("/index")
    //@RequestMapping(method = RequestMethod.GET) we can use this way as well
    public String getListOfAccounts(Model model){

        model.addAttribute("accounts",accountService.listAllAccounts());
        return "account/index";
    }

    @GetMapping("/create-form")
    //@RequestMapping(method = RequestMethod.GET) we can use this way as well
    public String getCreateAccountForm(Model model){
    //we need to provide empty account object
        model.addAttribute("account", Account.builder().build());

        //we need to provide accountType enum info for filling the dropdown options
        model.addAttribute("accountTypes", AccountType.values());
        return "account/create-account";
    }



    @PostMapping("/create-account")
    //@RequestMapping(method = RequestMethod.GET) we can use this way as well
    public String createAccountForm(@ModelAttribute("account") Account account){

        accountService.createAccount(account.getBalance(),
               new Date(), account.getAccountType(),account.getUserId());

        return "redirect:/index";
    }


    //deleting existing account
    @GetMapping("/delete/{accountId}")
    public String deleteAccountById( @PathVariable(value = "accountId") UUID accountId){

        accountService.deleteAccountById(accountId);
        return "redirect:/index";
    }

    //deleting existing account
    @GetMapping("/activate/{accountId}")
    public String activateDeletedAccountById( @PathVariable(value = "accountId") UUID accountId){

        accountService.activateDeletedAccountById(accountId);
        return "redirect:/index";
    }

}
