package com.cydeo.mapper;

import com.cydeo.dto.AccountDTO;
import com.cydeo.dto.TransactionDTO;
import com.cydeo.entity.Account;
import com.cydeo.entity.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    private final ModelMapper modelMapper;

    public TransactionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TransactionDTO convertToDTO(Transaction entity){
        //this method will accept Account and convert to DTO object by the help of model mapper class
        return modelMapper.map(entity,TransactionDTO.class);
    }

    public Transaction convertToEntity(TransactionDTO transactionDTO){
        //this method will accept AccountDTO and convert to entity object by the help of model mapper class
        return modelMapper.map(transactionDTO,Transaction.class);
    }

}
