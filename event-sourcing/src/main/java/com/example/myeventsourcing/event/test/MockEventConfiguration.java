package com.example.myeventsourcing.event.test;

import com.example.myeventsourcing.event.Event;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrador on 08/03/2016.
 */

@Configuration
public class MockEventConfiguration {

    @Bean
    public <E> Event<E> event() {
        return new EventBuilder<E>().getEvent();
    }

    class EventBuilder<E> {
        @Mock
        private Event<E> event;

        public Event<E> getEvent() {
            MockitoAnnotations.initMocks(this);
            return event;
        }
    }
}
