package com.cydeo.controller;

import com.cydeo.model.Account;
import com.cydeo.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        return "account/create-account";
    }



}
