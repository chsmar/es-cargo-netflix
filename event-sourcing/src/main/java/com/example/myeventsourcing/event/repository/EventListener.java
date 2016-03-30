package com.example.myeventsourcing.event.repository;

import com.example.myeventsourcing.event.BaseEvent;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

import java.util.Date;

/**
 * Created by Administrador on 03/03/2016.
 */
public class EventListener extends AbstractMongoEventListener<BaseEvent> {

    //changes onBeforeSave doesn't work.
    //Mongo doesn't save null fields.
    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseEvent> event) {
        if (event.getSource().getCreatedDate() == null) {
            event.getSource().setCreatedDate(new Date());
        }

        if (event.getSource().getType() == null) {
            event.getSource().setType(generateTypeOfEvent(event.getSource()));
        }
    }

    public static String generateTypeOfEvent(Object event) {
        return event.getClass().getSimpleName();
    }
}