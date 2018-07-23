package com.example;

import java.util.UUID;

public class ComplaintCreateCommand {

    private final UUID id;
    private final String company;
    private final String description;

    public ComplaintCreateCommand(UUID id, String company, String description) {
        this.id = id;
        this.company = company;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ComplaintCreateCommand{" +
                "id='" + id + '\'' +
                ", company='" + company + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
