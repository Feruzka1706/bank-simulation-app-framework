package com.cydeo.controller;

import com.cydeo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping
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

}
