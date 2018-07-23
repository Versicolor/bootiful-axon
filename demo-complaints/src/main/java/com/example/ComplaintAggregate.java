package com.example;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class ComplaintAggregate {

    private final static Logger LOG = LoggerFactory.getLogger(ComplaintAggregate.class);

    @AggregateIdentifier
    private UUID complaintId;

    private String description;

    public ComplaintAggregate() {
    }

    @CommandHandler
    public ComplaintAggregate(ComplaintCreateCommand command) {
        Assert.hasLength(command.getCompany());
        LOG.info("CommandHandler {}", command);

        apply(new ComplaintCreatedEvent(command.getId(), command.getCompany(), command.getDescription()));
    }

    @CommandHandler
    public void updateDescription(ComplaintDescriptionUpdateCommand command) {
        Assert.hasLength(command.getDescription());
        LOG.info("CommandHandler {}", command);

        apply(new ComplaintDescriptionUpdatedEvent(command.getId(), command.getDescription()));
    }

    @EventSourcingHandler
    public void on(ComplaintCreatedEvent event) {
        LOG.info("EventSourcingHandler {}", event);
        this.complaintId = event.getId();
    }

    @EventSourcingHandler
    public void on(ComplaintDescriptionUpdatedEvent event) {
        LOG.info("EventSourcingHandler {}", event);
        this.description = event.getDescription();
    }

}