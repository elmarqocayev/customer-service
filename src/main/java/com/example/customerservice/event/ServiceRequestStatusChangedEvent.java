package com.example.customerservice.event;

import com.example.customerservice.domain.RequestStatus;

/** Status dəyişəndə yayımlanan hadisə. Yalnız sadə dəyərlər saxlayır (lazy-loading problemi olmasın deyə). */
public record ServiceRequestStatusChangedEvent(
        Long requestId,
        String customerEmail,
        RequestStatus oldStatus,
        RequestStatus newStatus) {
}
