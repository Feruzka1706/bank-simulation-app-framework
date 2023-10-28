package com.cydeo.service.impl;

import com.cydeo.enums.AccountType;
import com.cydeo.exception.AccountOwnershipException;
import com.cydeo.exception.BadRequestException;
import com.cydeo.exception.BalanceNotSufficientException;
import com.cydeo.exception.UnderConstructionException;
import com.cydeo.model.Account;
import com.cydeo.model.Transaction;
import com.cydeo.repository.AccountRepository;
import com.cydeo.repository.TransactionRepository;
import com.cydeo.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class TransactionImpl implements TransactionService {

    @Value("${under_construction}")
    private boolean under_Construction;

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionImpl(AccountRepository accountRepository,
                           TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction makeTransfer(Account sender, Account receiver, BigDecimal amount, Date creationDate, String message) {
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
            Transaction transaction = Transaction.builder().transactionAmount(amount).sender(sender.getAccountId())
                    .receiver(receiver.getAccountId()).transactionDate(creationDate).message(message).build();


            //save into db  and return it
            return transactionRepository.save(transaction);
        }else{
           throw new UnderConstructionException("App is under construction, please try again later") ;
        }

    }

    private void executeBalanceAndUpdateIfRequired(BigDecimal amount, Account sender, Account receiver) {

        if(checkSenderBalance(sender,amount)){
            //update sender and receiver balance
            //100-80 = 20
            sender.setBalance(sender.getBalance().subtract(amount));
            // 50 + 80 = 130
            receiver.setBalance(receiver.getBalance().add(amount));
        }else {
            throw new BalanceNotSufficientException("Balance is not enough for this transfer");
        }

    }


    private boolean checkSenderBalance(Account sender, BigDecimal amount) {
        //verify sender has enough balance to send
        return sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) >= 0;

    }


    private void checkAccountOwnership(Account sender, Account receiver) {

        /*
        write a statement that checks if one of the account is saving,
        then user of sender or receiver is not the same, throw AccountOwnershipException
         */

        if( (sender.getAccountType().equals(AccountType.SAVING) || receiver.getAccountType().equals(AccountType.SAVING))
                && !(sender.getUserId().equals(receiver.getUserId()))){
            throw new AccountOwnershipException("Sender and Receiver MUST be same when sender's account type is Saving");
        }
    }



    private void validateAccount(Account sender, Account receiver) {

        /**
         * If any of the account is null
         * If account ids are the same (same account number)
         * If accounts exists in the database (repository)
         */
        if(sender==null || receiver==null){
            throw new BadRequestException("Sender or Receiver account cannot be null");
        }

        //if account are the same throw BadRequestException with saying accounts needs to be different
        if(sender.getAccountId().equals(receiver.getAccountId())){
            throw new BadRequestException("Sender and Receiver accounts MUST to be different");
        }

        //if accounts exist in database
        findAccountById(sender.getAccountId());
        findAccountById(receiver.getAccountId());

    }

    private void findAccountById(UUID accountId) {
       accountRepository.findById(accountId);
    }


    @Override
    public List<Transaction> findAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> last10Transactions() {

        //write a stream that sort the transactions based on creation date
        //and return only 10 last of them

        return transactionRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findTransactionListById(UUID transactionId) {
        return transactionRepository.findByTransactionId(transactionId);
    }
}
