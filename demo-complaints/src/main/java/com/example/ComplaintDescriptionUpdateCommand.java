package com.example;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

public class ComplaintDescriptionUpdateCommand {

    @TargetAggregateIdentifier
    private final UUID id;
    private final String description;

    public ComplaintDescriptionUpdateCommand(UUID id, String description) {
        this.id = id;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ComplaintDescriptionUpdateCommand{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
