package com.example.myeventsourcing.financial.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrador on 14/03/2016.
 */

@Entity
@Data
public class Invoice {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Date date;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Long customerId;
}
