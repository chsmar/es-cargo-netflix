package com.example.myeventsourcing.event;

import com.example.myeventsourcing.event.repository.EventListener;
import com.example.myeventsourcing.event.repository.EventRepository;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.mapping.model.CamelCaseAbbreviatingFieldNamingStrategy;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrador on 03/03/2016.
 */

@Configuration
@EnableMongoRepositories(basePackageClasses = EventRepository.class, mongoTemplateRef = "eventMongoTemplate")
public class MongoAutoConfiguration {

    protected String getDatabaseName() {
        return "evtsourcedb";
    }

    @Bean
    public MongoClient eventMongo() throws Exception {
        return new MongoClient();
    }

    @Bean
    public MongoTemplate eventMongoTemplate() throws Exception {
        return new MongoTemplate(eventMongoDbFactory(), eventMappingMongoConverter());
    }

    @Bean
    public MongoDbFactory eventMongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(eventMongo(), getDatabaseName());
    }

    @Bean
    public MappingMongoConverter eventMappingMongoConverter() throws Exception {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(eventMongoDbFactory());
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, eventMongoMappingContext());
        converter.setCustomConversions(eventCustomConversions());

        return converter;
    }

    @Bean
    public MongoMappingContext eventMongoMappingContext() throws ClassNotFoundException {

        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setInitialEntitySet(getInitialEntitySet());
        mappingContext.setSimpleTypeHolder(eventCustomConversions().getSimpleTypeHolder());
        mappingContext.setFieldNamingStrategy(fieldNamingStrategy());

        return mappingContext;
    }

    @Bean
    public CustomConversions eventCustomConversions() {
        return new CustomConversions(Collections.emptyList());
    }

    @Bean
    public EventListener eventMappingEventListener() {
        return new EventListener();
    }

    protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {

        String basePackage = getMappingBasePackage();
        Set<Class<?>> initialEntitySet = new HashSet<Class<?>>();

        if (StringUtils.hasText(basePackage)) {
            ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(
                    false);
            componentProvider.addIncludeFilter(new AnnotationTypeFilter(Document.class));
            componentProvider.addIncludeFilter(new AnnotationTypeFilter(Persistent.class));

            for (BeanDefinition candidate : componentProvider.findCandidateComponents(basePackage)) {
                initialEntitySet.add(ClassUtils.forName(candidate.getBeanClassName(),
                        AbstractMongoConfiguration.class.getClassLoader()));
            }
        }

        return initialEntitySet;
    }

    protected FieldNamingStrategy fieldNamingStrategy() {
        return abbreviateFieldNames() ? new CamelCaseAbbreviatingFieldNamingStrategy()
                : PropertyNameFieldNamingStrategy.INSTANCE;
    }

    protected boolean abbreviateFieldNames() {
        return false;
    }

    protected String getMappingBasePackage() {
        Package mappingBasePackage = getClass().getPackage();
        return mappingBasePackage == null ? null : mappingBasePackage.getName();
    }
}
