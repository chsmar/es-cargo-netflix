package net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.model;


import lombok.Data;

/**
 * Created by popikyardo on 20.10.15.
 */

@Data
public class CargoWebSocketEvent {

    private String eventId;
    private String eventType;
    private Object eventData;
    private String entityId;
}