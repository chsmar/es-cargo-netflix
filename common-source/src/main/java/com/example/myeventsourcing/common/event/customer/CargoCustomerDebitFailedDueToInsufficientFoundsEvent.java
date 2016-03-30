package com.example.myeventsourcing.common.event.customer;

import com.example.myeventsourcing.event.BaseEvent;

/**
 * Created by Administrador on 14/03/2016.
 */
public class CargoCustomerDebitFailedDueToInsufficientFoundsEvent extends BaseEvent {

    @Override
    public String getEntity() {
        return "com.example.myeventsourcing.customer.model.Customer";
    }
}
