package com.example.myeventsourcing.cargo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Administrador on 11/03/2016.
 */

@Entity
@Data
public class Cargo {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private CargoStatus status;

    @NotNull
    private BigDecimal amount;
}
