package com.example.myeventsourcing.event;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Administrador on 18/02/2016.
 */

@Data
@Document(collection = "event")
public class BaseEvent {

    private String id; //unique event id normally auto generated

    private String type; //event type typically the name of the class containing event data

    private String entity; //name of the aggregate's class

    private String entityId; //aggregate id (related entity)

    private Date createdDate; //timestamp at which the event happened

    private String txId; //groups events triggered in a single transaction

    private String userId; //optional id of the user or other actor which triggered the event
}
