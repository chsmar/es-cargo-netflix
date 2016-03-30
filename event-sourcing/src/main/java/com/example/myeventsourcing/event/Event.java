package com.example.myeventsourcing.event;

import com.example.myeventsourcing.event.repository.EventRepository;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;

public class Event<E> {

    private RabbitTemplate rabbitTemplate;

    private AmqpAdmin amqpAdmin;

    private MessageConverter defaultEventMessageConverter;

    private EventRepository eventRepository;

    public Event(RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin, MessageConverter defaultEventMessageConverter, EventRepository eventRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
        this.defaultEventMessageConverter = defaultEventMessageConverter;
        this.eventRepository = eventRepository;
    }

    public void fire(E event) {
        EventSourcingConfig config = EventSourcingConfig.create(event.getClass());
        amqpAdmin.declareExchange(config.getExchange());
        MessageConverter messageConverter = rabbitTemplate.getMessageConverter();
        rabbitTemplate.setMessageConverter(defaultEventMessageConverter);
        E saved = event;
        if(event instanceof BaseEvent) {
            saved = (E) eventRepository.save(event);
        }
        rabbitTemplate.convertAndSend(config.getExchangeName(), null, saved);
        rabbitTemplate.setMessageConverter(messageConverter);
    }
}
