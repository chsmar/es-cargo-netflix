package com.example.myeventsourcing.financial.service;

import com.example.myeventsourcing.common.event.customer.CargoCustomerDebitedEvent;
import com.example.myeventsourcing.common.event.financial.CargoInvoiceGeneratedEvent;
import com.example.myeventsourcing.event.Event;
import com.example.myeventsourcing.event.Listener;
import com.example.myeventsourcing.financial.model.Invoice;
import com.example.myeventsourcing.financial.service.repo.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrador on 14/03/2016.
 */

@Service
public class InvoiceService {
    
    @Autowired
    private Event<CargoInvoiceGeneratedEvent> invoiceGeneratedEventEvent; 

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Listener
    public void onCustomCargoDebited(CargoCustomerDebitedEvent event) {
        Long customerId = event.getCustomerId();
        BigDecimal amount = event.getAmount();
        Date date = new Date();

        Invoice invoice = generateInvoice(customerId, amount, date);
        CargoInvoiceGeneratedEvent generatedEvent = new CargoInvoiceGeneratedEvent(
                customerId,
                amount,
                date
        );
        generatedEvent.setEntityId(invoice.getId()+"");
        generatedEvent.setTxId(event.getTxId());

        invoiceGeneratedEventEvent.fire(generatedEvent);
    }

    private Invoice generateInvoice(Long customerId, BigDecimal amount, Date date) {
        Invoice instance = new Invoice();
        instance.setCustomerId(customerId);
        instance.setDate(date);
        instance.setAmount(amount);

        return invoiceRepository.saveAndFlush(instance);
    }
}
