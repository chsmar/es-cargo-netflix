package com.example.myeventsourcing.event;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by Administrador on 16/02/2016.
 */
public class EventListenerAnnotationBeanPostProcessor extends RabbitListenerAnnotationBeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        final Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                Listener listener = AnnotationUtils.findAnnotation(method, Listener.class);
                if (listener != null) {
                    Class<?> eventClass = method.getParameterTypes()[0];
                    RabbitListener rabbitListener = createRabbitListener(EventSourcingConfig.create(eventClass, targetClass, method));
                    //System.out.println("PROCESSING: " + beanName + "____" + rabbitListener + "____" + targetClass + "____" + bean + "____" + targetClass.getSuperclass());
                    processAmqpListener(rabbitListener, method, bean, beanName);
                }
            }
        });
        return super.postProcessAfterInitialization(bean, beanName);
    }

    private RabbitListener createRabbitListener(final EventSourcingConfig config) {
        return new RabbitListener() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return RabbitListener.class;
            }

            @Override
            public String id() {
                return "";
            }

            @Override
            public String containerFactory() {
                return "";
            }

            @Override
            public String[] queues() {
                return new String[0];
            }

            @Override
            public boolean exclusive() {
                return false;
            }

            @Override
            public String priority() {
                return "";
            }

            @Override
            public String admin() {
                return "";
            }

            @Override
            public QueueBinding[] bindings() {
                return new QueueBinding[]{createQueueBinding(config)};
            }

            @Override
            public String group() {
                return "";
            }
        };
    }

    private QueueBinding createQueueBinding(final EventSourcingConfig config) {
        return new QueueBinding() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return QueueBinding.class;
            }

            @Override
            public Queue value() {
                return createQueue(config);
            }

            @Override
            public Exchange exchange() {
                return createExchange(config);
            }

            @Override
            public String key() {
                return "";
            }
        };
    }

    private Queue createQueue(final EventSourcingConfig config) {
        return new Queue() {
            @Override
            public String value() {
                return config.getQueue().getName();
            }

            @Override
            public String durable() {
                return config.getQueue().isDurable() + "";
            }

            @Override
            public String exclusive() {
                return config.getQueue().isExclusive() + "";
            }

            @Override
            public String autoDelete() {
                return config.getQueue().isAutoDelete() + "";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Queue.class;
            }
        };
    }

    private Exchange createExchange(final EventSourcingConfig config) {
        return new Exchange() {
            @Override
            public String value() {
                return config.getExchange().getName();
            }

            @Override
            public String type() {
                return config.getExchange().getType();
            }

            @Override
            public String durable() {
                return config.getExchange().isDurable() + "";
            }

            @Override
            public String autoDelete() {
                return config.getExchange().isAutoDelete() + "";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };
    }
}
