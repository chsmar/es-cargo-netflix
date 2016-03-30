package com.example.myeventsourcing.cargo.service;

import com.example.myeventsourcing.cargo.model.Cargo;
import com.example.myeventsourcing.cargo.model.CargoStatus;
import com.example.myeventsourcing.cargo.service.repo.CargoRepository;
import com.example.myeventsourcing.common.event.cargo.CargoCreatedEvent;
import com.example.myeventsourcing.common.event.financial.CargoInvoiceGeneratedEvent;
import com.example.myeventsourcing.event.Event;
import com.example.myeventsourcing.event.Listener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrador on 11/03/2016.
 */

@Service
public class CargoService {

    @Autowired
    private Event<CargoCreatedEvent> cargoCreatedEventEvent;

    @Autowired
    private CargoRepository cargoRepository;

    public void createCargo(Cargo cargo) {
        cargo.setStatus(CargoStatus.CREATED);
        cargo = cargoRepository.saveAndFlush(cargo);

        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        CargoCreatedEvent createdEvent = new CargoCreatedEvent(cargo.getCustomerId(), cargo.getAmount());
        createdEvent.setEntityId(cargo.getId() + "");
        createdEvent.setTxId(cargo.getId()+"");
        cargoCreatedEventEvent.fire(createdEvent);
    }

    @Listener
    public void onInvoiceGenerated(CargoInvoiceGeneratedEvent event) {
        Cargo cargo = cargoRepository.findOne(Long.parseLong(event.getTxId()));
        cargo.setStatus(CargoStatus.RECEIVED);
        cargoRepository.saveAndFlush(cargo);
    }

    public List<Cargo> findAll() {
        return cargoRepository.findByStatus(CargoStatus.RECEIVED);
    }
}
