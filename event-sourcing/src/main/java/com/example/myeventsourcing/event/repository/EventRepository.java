package com.example.myeventsourcing.event.repository;

import com.example.myeventsourcing.event.BaseEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Administrador on 02/03/2016.
 */
public interface EventRepository<E extends BaseEvent> extends MongoRepository<E, String> {
}
