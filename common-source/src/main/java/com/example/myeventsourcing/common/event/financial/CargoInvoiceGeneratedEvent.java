package com.example.myeventsourcing.common.event.financial;

import com.example.myeventsourcing.event.BaseEvent;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrador on 14/03/2016.
 */

@Data
public class CargoInvoiceGeneratedEvent extends BaseEvent {

    private Long customerId;

    private BigDecimal amount;

    private Date date;

    public CargoInvoiceGeneratedEvent(Long customerId, BigDecimal amount, Date date) {
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
    }

    public CargoInvoiceGeneratedEvent() {
    }

    @Override
    public String getEntity() {
        return "com.example.myeventsourcing.financial.model.Invoice";
    }
}
