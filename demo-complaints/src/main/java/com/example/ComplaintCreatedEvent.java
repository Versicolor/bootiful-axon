package com.example;

import java.util.UUID;

public class ComplaintCreatedEvent {
    private UUID id;
    private String company;
    private String description;

    public ComplaintCreatedEvent() {
    }

    public ComplaintCreatedEvent(UUID id, String company, String description) {
        this.id = id;
        this.company = company;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ComplaintCreatedEvent{" +
                "id='" + id + '\'' +
                ", company='" + company + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
