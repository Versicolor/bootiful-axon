package com.example;

import com.mongodb.MongoClient;
import org.axonframework.config.Configuration;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.mongo.eventsourcing.tokenstore.MongoTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class DemoComplaintsViewApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoComplaintsViewApplication.class, args);
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
    public TokenStore tokenStore() {
       return new MongoTokenStore(axonMongoTemplate(), new XStreamSerializer());
    }

    @Autowired
    public void configure(EventProcessingConfiguration config) {
       config.registerTrackingEventProcessor("view", conf -> TrackingEventProcessorConfiguration.forSingleThreadedProcessing().andInitialSegmentsCount(1));
    }

    @Bean
    public MongoTemplate axonMongoTemplate() {
        return new DefaultMongoTemplate(mongoClient);
    }

    @Bean
    public EventStorageEngine eventStorageEngine(Serializer serializer) {
        return new MongoEventStorageEngine(serializer, null, axonMongoTemplate(), new DocumentPerEventStorageStrategy());
    }



    /*
        @Bean
    public SnapshotTriggerDefinition snapshotTriggerDefinition(Snapshotter snapshotter) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, SNAPSHOT_THRESHOLD);
    }
     */

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
