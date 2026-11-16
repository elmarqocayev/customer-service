package com.example.customerservice.repository;

import com.example.customerservice.domain.RequestStatus;
import com.example.customerservice.domain.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    Page<ServiceRequest> findByStatus(RequestStatus status, Pageable pageable);
    boolean existsByCustomerId(Long customerId);
}
