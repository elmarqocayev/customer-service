package com.example.customerservice.event;

import com.example.customerservice.domain.RequestStatus;

public record ServiceRequestStatusChangedEvent(
        Long requestId,
        String customerEmail,
        RequestStatus oldStatus,
        RequestStatus newStatus) {
}
