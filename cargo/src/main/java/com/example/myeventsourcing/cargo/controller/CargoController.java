package com.example.myeventsourcing.cargo.controller;

import com.example.myeventsourcing.cargo.model.Cargo;
import com.example.myeventsourcing.cargo.service.CargoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrador on 15/03/2016.
 */

@RestController
@RequestMapping("/api/cargo")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Cargo> getCargos() {
        return cargoService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void receive(@RequestBody Cargo cargo) {
        cargoService.createCargo(cargo);
    }
}
