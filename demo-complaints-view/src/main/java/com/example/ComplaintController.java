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

    private final ComplaintRepository complaintRepository;

    public ComplaintController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @GetMapping
    public List<ComplaintEntity> findAll() {
        List<ComplaintEntity> res = new ArrayList<>();
        complaintRepository.findAll().forEach(res::add);
        return res;
    }

    @GetMapping("/{id}")
    public ComplaintEntity find(@PathVariable UUID id) {
        return complaintRepository.findOne(id);
    }

    @GetMapping("/company/{val}")
    public List<ComplaintEntity> findCompanies(@PathVariable String val) {
        List<ComplaintEntity> res = new ArrayList<>();
        complaintRepository.findComplaintEntityByCompany(val).forEach(res::add);
        return res;
    }
}