package com.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPPublisher;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.EventHandlingConfiguration;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.config.SagaConfiguration;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.axonframework.eventhandling.saga.repository.SagaStore;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.*;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.annotation.ParameterResolverFactory;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventhandling.saga.repository.MongoSagaStore;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.StorageStrategy;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotter;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@SpringBootApplication
@EnableMongoRepositories
public class DemoComplaintsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoComplaintsApplication.class, args);
    }

    @Autowired
    private MongoClient mongoClient;

   /*@Bean
    public Serializer messageSerializer() {
       return new JacksonSerializer();
    }

    @Bean
    public Serializer eventSerializer() {
        return new JacksonSerializer();
    }*/


    @Bean
    public EventStore eventStore(EventStorageEngine eventStorageEngine) {
        return new EmbeddedEventStore(eventStorageEngine);
    }

    @Bean
    public TokenStore tokenStore(MongoTemplate mongoTemplate) {
       return new MongoTokenStore(mongoTemplate, new XStreamSerializer());
    }

    /*@Autowired
    public void configure(EventProcessingConfiguration config) {
        config.usingTrackingProcessors();
    }*/

    @Bean
    public MongoTemplate axonMongoTemplate() {
        return new DefaultMongoTemplate(mongoClient);
    }

    @Bean
    public EventStorageEngine eventStorageEngine(MongoTemplate mongoTemplate, Serializer serializer) {
        return new MongoEventStorageEngine(serializer, null, mongoTemplate, new DocumentPerEventStorageStrategy());
    }

    @Bean
    public Repository<ComplaintAggregate> complaintAggregateRepository(EventStore eventStore, SnapshotTriggerDefinition snapshotTriggerDefinition) {
        return new EventSourcingRepository<>(ComplaintAggregate.class, eventStore, snapshotTriggerDefinition);
    }

    @Bean
    public SpringAggregateSnapshotterFactoryBean snapshotter() {
        return new SpringAggregateSnapshotterFactoryBean();
    }

    @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, 5);
    }

    @Bean
    public SagaStore sagaStore(MongoTemplate mongoTemplate) {
        return new MongoSagaStore(mongoTemplate, new XStreamSerializer());
    }

    @Bean
    public SagaConfiguration sagaConfiguration() {
        return SagaConfiguration.trackingSagaManager(SagaTest.class)
                .configureTrackingProcessor(configuration -> TrackingEventProcessorConfiguration.forParallelProcessing(2));
    }

    // Message

    /*@Bean
    public SpringAMQPPublisher springAMQPPublisher() {
        SpringAMQPPublisher springAMQPPublisher = new SpringAMQPPublisher(eventStore());
        springAMQPPublisher.setExchange(exchange());
        springAMQPPublisher.setSerializer(messageSerializer());
        return springAMQPPublisher;
    }

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.fanoutExchange("Complaints").build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable("Complaints").build();
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("*").noargs();
    }

    @Autowired
    public void configure(AmqpAdmin admin) {
        admin.declareExchange(exchange());
        admin.declareQueue(queue());
        admin.declareBinding(binding());
    }*/
}
