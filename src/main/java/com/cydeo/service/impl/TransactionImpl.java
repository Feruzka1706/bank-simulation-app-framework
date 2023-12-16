package com.cydeo.service.impl;

import com.cydeo.dto.AccountDTO;
import com.cydeo.dto.TransactionDTO;
import com.cydeo.entity.Transaction;
import com.cydeo.enums.AccountType;
import com.cydeo.exception.AccountOwnershipException;
import com.cydeo.exception.BadRequestException;
import com.cydeo.exception.BalanceNotSufficientException;
import com.cydeo.exception.UnderConstructionException;
import com.cydeo.mapper.TransactionMapper;
import com.cydeo.repository.AccountRepository;
import com.cydeo.repository.TransactionRepository;
import com.cydeo.service.AccountService;
import com.cydeo.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class TransactionImpl implements TransactionService {

    @Value("${under_construction}")
    private boolean under_Construction;

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionImpl(AccountService accountService,
                           TransactionRepository transactionRepository,
                           TransactionMapper transactionMapper) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Override
    public TransactionDTO makeTransfer(AccountDTO sender, AccountDTO receiver, BigDecimal amount, Date creationDate, String message) {
        if(!under_Construction) {
            /** If sender or receiver is null ?
             *  If sender and receiver is the same account ?
             *  If sender has enough balance to make transfer ?
             *  If both accounts are checking, if not, one of them saving, it needs to be same userId
             */

            validateAccount(sender, receiver);
            checkAccountOwnership(sender, receiver);
            executeBalanceAndUpdateIfRequired(amount, sender, receiver);

            //makeTransfer
        /*
        After all validations are completed, and money is transferred,
        we need to create Transaction object and save/return it
         */
            TransactionDTO transactionDTO = new TransactionDTO(sender,receiver,amount,creationDate,message);

            //save into db  and return it
            transactionRepository.save(transactionMapper.convertToEntity(transactionDTO));
            return transactionDTO;
        }else{
           throw new UnderConstructionException("App is under construction, please try again later") ;
        }

    }

    private void executeBalanceAndUpdateIfRequired(BigDecimal amount, AccountDTO sender, AccountDTO receiver) {

        if(checkSenderBalance(sender,amount)){
            //update sender and receiver balance
            //100-80 = 20
            sender.setBalance(sender.getBalance().subtract(amount));
            // 50 + 80 = 130
            receiver.setBalance(receiver.getBalance().add(amount));
            /** get the entity from db for both sender and receiver, update balance and save it
             * create accountService updateAccount method and use it for saving
             */
            // method is updating given accountId balance and save it for DB
            //accountService.updateAccount(accountService.findAccountById(sender.getAccountId()));
            AccountDTO senderAccount = accountService.findAccountById(receiver.getId());
            senderAccount.setBalance(sender.getBalance());
            accountService.updateAccount(senderAccount);

            // method is updating given accountId balance and save it for DB
            //accountService.updateAccount(accountService.findAccountById(receiver.getAccountId()));
            AccountDTO receiverAccount = accountService.findAccountById(receiver.getId());
            receiverAccount.setBalance(receiver.getBalance());
            accountService.updateAccount(receiverAccount);

        }else {
            throw new BalanceNotSufficientException("Balance is not enough for this transfer");
        }

    }


    private boolean checkSenderBalance(AccountDTO sender, BigDecimal amount) {
        //verify sender has enough balance to send
        return sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >= 0;

    }


    private void checkAccountOwnership(AccountDTO sender, AccountDTO receiver) {

        /*
        write a statement that checks if one of the account is saving,
        then user of sender or receiver is not the same, throw AccountOwnershipException
         */

        if( (sender.getAccountType().equals(AccountType.SAVING) || receiver.getAccountType().equals(AccountType.SAVING))
                && !(sender.getUserId().equals(receiver.getUserId()))){
            throw new AccountOwnershipException("Sender and Receiver MUST be same when sender's account type is Saving");
        }
    }



    private void validateAccount(AccountDTO sender, AccountDTO receiver) {

        /**
         * If any of the account is null
         * If account ids are the same (same account number)
         * If accounts exists in the database (repository)
         */
        if(sender==null || receiver==null){
            throw new BadRequestException("Sender or Receiver account cannot be null");
        }

        //if account are the same throw BadRequestException with saying accounts needs to be different
        if(sender.getId().equals(receiver.getId())){
            throw new BadRequestException("Sender and Receiver accounts MUST to be different");
        }

        //if accounts exist in database
        findAccountById(sender.getId());
        findAccountById(receiver.getId());

    }

    private void findAccountById(Long accountId) {
       accountService.findAccountById(accountId);
    }

    @Override
    public List<TransactionDTO> findAllTransactions() {

        return transactionRepository.findAll().stream()
                .map(transactionMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> last10Transactions() {

        //write a stream that sort the transactions based on creation date
        //and return only 10 last of them

        // Convert the list of transactions to DTOs using your mapper
        return transactionRepository.findTop10ByOrderByTransactionDateDesc()
                .stream()
                .map(transactionMapper::convertToDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<TransactionDTO> findTransactionListById(Long accountId) {

        //get the list of transactions if account id is involved as a sender or receiver
        return transactionRepository.findAllTransactionsByAccountId(accountId)
                .stream().map(transactionMapper::convertToDTO)
                .collect(Collectors.toList());
    }
}
