package com.example;

import java.util.UUID;

public class ComplaintDescriptionUpdatedEvent {
    private UUID id;
    private String description;

    public ComplaintDescriptionUpdatedEvent() {
    }

    public ComplaintDescriptionUpdatedEvent(UUID id, String description) {
        this.id = id;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ComplaintDescriptionUpdatedEvent{" +
                "id=" + id +
                ", description='" + description + '\'' +
                '}';
    }
}
