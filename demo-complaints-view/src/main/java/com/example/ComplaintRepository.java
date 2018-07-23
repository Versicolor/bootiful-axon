package com.example;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ComplaintRepository extends PagingAndSortingRepository<ComplaintEntity, UUID> {

    List<ComplaintEntity> findComplaintEntityByCompany(String company);
}
