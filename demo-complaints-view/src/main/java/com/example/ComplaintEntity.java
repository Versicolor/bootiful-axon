package com.example;

import org.springframework.data.annotation.Id;

import java.util.UUID;

public class ComplaintEntity {

    @Id
    private UUID id;
    private String company;
    private String description;

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
}
