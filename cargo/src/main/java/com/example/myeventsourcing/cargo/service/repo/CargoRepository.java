package com.example.myeventsourcing.cargo.service.repo;

import com.example.myeventsourcing.cargo.model.Cargo;
import com.example.myeventsourcing.cargo.model.CargoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrador on 14/03/2016.
 */
public interface CargoRepository extends JpaRepository<Cargo, Long> {

    //test
    List<Cargo> findByStatus(CargoStatus status);
}
