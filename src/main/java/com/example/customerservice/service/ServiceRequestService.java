package com.example.customerservice.service;

import com.example.customerservice.domain.RequestStatus;
import com.example.customerservice.dto.ServiceRequestCreate;
import com.example.customerservice.dto.ServiceRequestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceRequestService {
    ServiceRequestResponse create(ServiceRequestCreate request);
    ServiceRequestResponse get(Long id);
    Page<ServiceRequestResponse> list(RequestStatus status, Pageable pageable);
    ServiceRequestResponse changeStatus(Long id, RequestStatus newStatus);
}
