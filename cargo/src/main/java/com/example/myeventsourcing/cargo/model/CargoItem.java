package com.example.myeventsourcing.cargo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Administrador on 14/03/2016.
 */

@Entity
@Data
public class CargoItem {

    @Id
    @GeneratedValue
    private Long id;

    private String description;
}
