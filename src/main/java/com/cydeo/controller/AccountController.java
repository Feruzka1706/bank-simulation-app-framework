package com.cydeo.controller;

import com.cydeo.dto.AccountDTO;
import com.cydeo.enums.AccountType;
import com.cydeo.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
//@RequestMapping we don't have to explicitly mention RequestMapping if we are using @GetMapping in method level
public class AccountController {

    /*
      write a method to return index.html including account list information
      endpoint:index
   */
    AccountService accountService;

    /*
    write a method to return index.html page including account list information
    endpoint:index
     */
    @GetMapping("/index")
    public String getIndexPage(Model model){

        model.addAttribute("accountList",accountService.listAllAccount());
        return "account/index";
    }

    @GetMapping("/create-form")
    //@RequestMapping(method = RequestMethod.GET) we can use this way as well
    public String getCreateAccountForm(Model model){
    //we need to provide empty account object
        model.addAttribute("accountDTO", new AccountDTO());

        //we need to provide accountType enum info for filling the dropdown options
        model.addAttribute("accountTypes", AccountType.values());
        return "account/create-account";
    }


    //create a method to capture information from ui
    //print them on the console.
    //trigger createNewAccount method, create the account based on the user input.
    //once user created return back to the index page.
    @PostMapping("/create-account")
    //@RequestMapping(method = RequestMethod.GET) we can use this way as well
    public String createAccountForm(@ModelAttribute("accountDTO") @Valid AccountDTO accountDTO,
                                    BindingResult bindingResult, Model model){
        //we have to put BindingResult right after validation object
        if(bindingResult.hasErrors()){
            model.addAttribute("accountTypes", AccountType.values());
            return "account/create-account";
        }
        accountService.createNewAccount(accountDTO);

        return "redirect:/index";
    }


    //deleting existing account
    @GetMapping("/delete/{accountId}")
    public String deleteAccountById( @PathVariable(value = "accountId") Long accountId){

        accountService.deleteAccountById(accountId);
        return "redirect:/index";
    }

    //deleting existing account
    @GetMapping("/activate/{accountId}")
    public String activateDeletedAccountById( @PathVariable(value = "accountId") Long accountId){

        accountService.activateDeletedAccountById(accountId);
        return "redirect:/index";
    }

}
