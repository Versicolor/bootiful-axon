package com.example;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("view")
public class ComplaintEventHandler {

    private final static Logger LOG = LoggerFactory.getLogger(ComplaintEventHandler.class);

    @Autowired
    private ComplaintRepository complaintRepository;

    @EventHandler
    public void on(ComplaintCreatedEvent event) {
        LOG.info("EventHandler {}", event);

        ComplaintEntity complaintEntity = new ComplaintEntity();
        complaintEntity.setId(event.getId());
        complaintEntity.setCompany(event.getCompany());
        complaintEntity.setDescription(event.getDescription());
        complaintRepository.save(complaintEntity);
    }

    @EventHandler
    public void on(ComplaintDescriptionUpdatedEvent event) {
        LOG.info("EventHandler {}", event);

        ComplaintEntity complaintEntity = complaintRepository.findOne(event.getId());
        complaintEntity.setDescription(event.getDescription());
        complaintRepository.save(complaintEntity);
    }
}
