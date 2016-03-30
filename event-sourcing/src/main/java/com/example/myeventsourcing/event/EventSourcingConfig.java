package com.example.myeventsourcing.event;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;

import java.lang.reflect.Method;

/**
 * Created by Administrador on 16/02/2016.
 */
public class EventSourcingConfig {

    private String exchangeName;

    private Exchange exchange;

    private Queue queue;

    public static EventSourcingConfig create(Class<?> source) {
        String exchangeName = source.getSimpleName();
        EventSourcingConfig config = new EventSourcingConfig();
        config.exchange = new FanoutExchange(exchangeName);
        config.exchangeName = exchangeName;

        return config;
    }

    public static EventSourcingConfig create(Class<?> source, Class<?> target, Method targetMethod) {
        EventSourcingConfig config = create(source);
        String queueName = target.getSimpleName()+"."+targetMethod.getName();
        config.queue = new Queue(queueName);

        return config;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public Exchange getExchange() {
        return exchange;
    }

    public Queue getQueue() {
        return queue;
    }
}
