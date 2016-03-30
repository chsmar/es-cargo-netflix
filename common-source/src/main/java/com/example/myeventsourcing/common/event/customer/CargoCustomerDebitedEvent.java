package com.example.myeventsourcing.common.event.customer;

import com.example.myeventsourcing.event.BaseEvent;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Administrador on 14/03/2016.
 */

@Data
public class CargoCustomerDebitedEvent extends BaseEvent {
    private Long customerId;

    private BigDecimal amount;

    public CargoCustomerDebitedEvent(Long customerId, BigDecimal amount) {
        this.customerId = customerId;
        this.amount = amount;
    }

    public CargoCustomerDebitedEvent() {
    }

    @Override
    public String getEntity() {
        return "com.example.myeventsourcing.customer.model.Customer";
    }
}
