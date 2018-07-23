package com.example;

import com.mongodb.MongoClient;
import org.axonframework.config.EventProcessingConfiguration;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoComplaintsStatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoComplaintsStatsApplication.class, args);
	}

	@Value("${server.port}")
	private int serverPort;



	/*@Bean
	public SpringAMQPMessageSource statisticsQueue(Serializer serializer) {
		return new SpringAMQPMessageSource(serializer) {

			@RabbitListener(queues = "Complaints")
			@Override
			public void onMessage(Message message, Channel channel) {
				super.onMessage(message, channel);
			}
		};
	}*/

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
		config.usingTrackingProcessors();
		config.assignProcessingGroup("statistics", "statistics" + serverPort);
	}

	@Bean
	public MongoTemplate axonMongoTemplate() {
		return new DefaultMongoTemplate(mongoClient);
	}

	@Bean
	public EventStorageEngine eventStorageEngine(Serializer serializer) {
		return new MongoEventStorageEngine(serializer, null, axonMongoTemplate(), new DocumentPerEventStorageStrategy());
	}
}
