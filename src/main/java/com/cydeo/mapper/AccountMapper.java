package com.cydeo.mapper;

import com.cydeo.dto.AccountDTO;
import com.cydeo.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    private final ModelMapper modelMapper;

    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AccountDTO convertToDTO(Account entity){
        //this method will accept Account and convert to DTO object by the help of model mapper class
        return modelMapper.map(entity,AccountDTO.class);
    }

    public Account convertToEntity(AccountDTO accountDTO){
        //this method will accept AccountDTO and convert to entity object by the help of model mapper class
        return modelMapper.map(accountDTO,Account.class);
    }

}
