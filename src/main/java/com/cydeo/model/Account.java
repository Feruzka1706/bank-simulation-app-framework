package com.cydeo.model;


import com.cydeo.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Account {

    private UUID accountId;
    private BigDecimal balance;
    private AccountType accountType;
    private Date creationDate;
    private Long userId;

}