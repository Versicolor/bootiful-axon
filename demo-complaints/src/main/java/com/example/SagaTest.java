package com.example;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

// TODO: if the application shtdown in middle of saga, everything is stuck
@Saga
public class SagaTest {

    private final static Logger LOG = LoggerFactory.getLogger(SagaTest.class);

    private boolean descriptionUpdated = false;

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    public void handle(ComplaintCreatedEvent event) {
        LOG.info("Saga start {}", event);
        commandGateway.send(new ComplaintDescriptionUpdateCommand(event.getId(), "saga !"));
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "id")
    public void handle(ComplaintDescriptionUpdatedEvent event) {
        LOG.info("Saga end {}", event);
        descriptionUpdated = true;
    }
}
