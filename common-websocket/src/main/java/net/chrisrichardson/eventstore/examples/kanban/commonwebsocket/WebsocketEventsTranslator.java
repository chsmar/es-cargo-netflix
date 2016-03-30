package net.chrisrichardson.eventstore.examples.kanban.commonwebsocket;

import com.example.myeventsourcing.common.event.customer.CargoCustomerDebitFailedDueToInsufficientFoundsEvent;
import com.example.myeventsourcing.common.event.financial.CargoInvoiceGeneratedEvent;
import com.example.myeventsourcing.event.BaseEvent;
import com.example.myeventsourcing.event.Listener;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chrisrichardson.eventstore.examples.kanban.commonwebsocket.model.CargoWebSocketEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by popikyardo on 15.10.15.
 */

public class WebsocketEventsTranslator {

    protected SimpMessagingTemplate template;

    private static String DESTINATION_DEFAULT_URL = "/events";

    private static ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static Logger log = LoggerFactory.getLogger(WebsocketEventsTranslator.class);

    public WebsocketEventsTranslator(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Listener
    public void sendCargoInvoiceGenerated(CargoInvoiceGeneratedEvent event) throws JsonProcessingException {
        Map<String, Object> invoice = new HashMap<>();
        invoice.put("customerId", event.getCustomerId());
        invoice.put("date", event.getDate());
        invoice.put("amount", event.getAmount());

        this.sendEvent(event, DESTINATION_DEFAULT_URL, invoice);
    }

    @Listener
    public void sendCargoCustomerDebitFailed(CargoCustomerDebitFailedDueToInsufficientFoundsEvent event) throws JsonProcessingException {
        Map<String, Object> debitFailed = new HashMap<>();

        this.sendEvent(event, DESTINATION_DEFAULT_URL, debitFailed);
    }

    private void sendEvent(BaseEvent de, String destination, Object eventData) throws JsonProcessingException {
        log.info("Sending board event to websocket : {}", de);
        CargoWebSocketEvent event = new CargoWebSocketEvent();
        event.setEntityId(de.getEntityId());
        event.setEventData(eventData);
        event.setEventId(de.getId());
        event.setEventType(de.getType());
        template.convertAndSend(destination, event);
    }
}