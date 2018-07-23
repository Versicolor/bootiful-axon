package com.example;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@ProcessingGroup("statistics")
@RestController
public class StatsController {

    private final static Logger LOG = LoggerFactory.getLogger(StatsController.class);


    private final ConcurrentMap<String, AtomicLong> statsCreated = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, AtomicLong> statsUpdated = new ConcurrentHashMap<>();


    @EventHandler
    public void on(ComplaintCreatedEvent event) {
        LOG.info("EventHandler {}", event);

        statsCreated.computeIfAbsent(event.getCompany(), k -> new AtomicLong()).incrementAndGet();
    }

    @EventHandler
    public void on(ComplaintDescriptionUpdatedEvent event) {
        LOG.info("EventHandler {}", event);

        statsUpdated.computeIfAbsent(event.getId(), k -> new AtomicLong()).incrementAndGet();
    }

    @GetMapping("/created")
    public ConcurrentMap<String, AtomicLong> showCreated() {
        return statsCreated;
    }

    @GetMapping("/updated")
    public ConcurrentMap<UUID, AtomicLong> showUpdated() {
        return statsUpdated;
    }
}
