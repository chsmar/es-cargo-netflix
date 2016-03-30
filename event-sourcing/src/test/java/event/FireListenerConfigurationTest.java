package event;

import com.example.myeventsourcing.event.BaseEvent;
import com.example.myeventsourcing.event.Event;
import com.example.myeventsourcing.event.EventSourcingAutoConfiguration;
import com.example.myeventsourcing.event.Listener;
import com.example.myeventsourcing.event.repository.EventRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Administrador on 07/03/2016.
 */

@ContextConfiguration(classes = {RabbitAutoConfiguration.class, EventSourcingAutoConfiguration.class, FireListenerConfigurationTest.Config.class})
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
public class FireListenerConfigurationTest {

    @Autowired
    private Event<TestFiredEvent> event;

    @Autowired
    private TestService testService;

    @Test
    public void testFireEvent() throws Exception {
        Assert.assertNull(testService.noFiredEvent);
        Assert.assertNull(testService.event);
        event.fire(new TestFiredEvent());
        testService.delay();
        Assert.assertNull(testService.noFiredEvent);
        Assert.assertNotNull(testService.event);
    }

    public static class TestFiredEvent extends BaseEvent {

    }

    public static class TestNoFiredEvent extends BaseEvent {

    }

    @Configuration
    public static class Config {

        @Mock
        private EventRepository eventRepository;

        /*@Bean
        public ConnectionFactory rabbitConnectionFactory() {
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
            connectionFactory.setHost("localhost");
            return connectionFactory;
        }

        @Bean
        public AmqpAdmin amqpAdmin() {
            return new RabbitAdmin(rabbitConnectionFactory());
        }

        @Bean
        public RabbitTemplate rabbitTemplate() {
            RabbitTemplate template = new RabbitTemplate(rabbitConnectionFactory());
            return template;
        }

        @Bean
        public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
                ConnectionFactory connectionFactory, RabbitProperties config) {
            SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
            factory.setConnectionFactory(connectionFactory);
            return factory;
        }*/

        @Bean
        public EventRepository eventRepository() {
            MockitoAnnotations.initMocks(this);
            return eventRepository;
        }

        @Bean
        @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
        public TestService testService() {
            return new TestService();
        }
    }

    public static class TestService {

        private TestFiredEvent event;

        private TestNoFiredEvent noFiredEvent;

        @Listener
        public void listener(TestFiredEvent e) {
            this.event = e;
        }

        @Listener
        public void listenerNoFired(TestNoFiredEvent e) {
            this.noFiredEvent = e;
        }

        public void delay() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
