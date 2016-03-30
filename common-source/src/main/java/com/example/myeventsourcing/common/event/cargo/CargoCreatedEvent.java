package com.example.myeventsourcing.common.event.cargo;

import com.example.myeventsourcing.event.BaseEvent;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Administrador on 11/03/2016.
 */

@Data
public class CargoCreatedEvent extends BaseEvent {
    private Long customerId;

    private BigDecimal amount;

    public CargoCreatedEvent(Long customerId, BigDecimal amount) {
        this.customerId = customerId;
        this.amount  = amount;
    }

    public CargoCreatedEvent() {
    }

    @Override
    public String getEntity() {
        return "com.example.myeventsourcing.cargo.model.Cargo";
    }
}
