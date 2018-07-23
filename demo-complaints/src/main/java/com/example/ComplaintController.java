package com.example;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RequestMapping("/complaints")
@RestController
public class ComplaintController {

    private final CommandGateway commandGateway;

    public ComplaintController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public CompletableFuture<String> createComplaint(@RequestBody Map<String, String> request) {
        UUID id = UUID.randomUUID();
        return commandGateway.send(new ComplaintCreateCommand(id, request.get("company"), request.get("description")));
    }

    @PutMapping("/{id}")
    public CompletableFuture<String> updateComplaintDescription(@PathVariable UUID id, @RequestBody Map<String, String> request) {
        return commandGateway.send(new ComplaintDescriptionUpdateCommand(id, request.get("description")));
    }
}