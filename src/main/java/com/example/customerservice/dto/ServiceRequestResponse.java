package com.example.customerservice.dto;

import com.example.customerservice.domain.Priority;
import com.example.customerservice.domain.RequestStatus;
import com.example.customerservice.domain.ServiceRequest;
import com.example.customerservice.domain.Tag;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public record ServiceRequestResponse(
        Long id,
        String title,
        String description,
        RequestStatus status,
        Priority priority,
        Long customerId,
        Set<String> tags,
        Instant createdAt,
        Instant updatedAt) {

    public static ServiceRequestResponse from(ServiceRequest r) {
        return new ServiceRequestResponse(
                r.getId(), r.getTitle(), r.getDescription(), r.getStatus(), r.getPriority(),
                r.getCustomer().getId(),
                r.getTags().stream().map(Tag::getName).collect(Collectors.toSet()),
                r.getCreatedAt(), r.getUpdatedAt());
    }
}
