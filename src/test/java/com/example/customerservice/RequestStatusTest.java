package com.example.customerservice;

import com.example.customerservice.domain.RequestStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestStatusTest {

    @Test
    void openCanGoToInProgress() {
        assertTrue(RequestStatus.OPEN.canTransitionTo(RequestStatus.IN_PROGRESS));
    }

    @Test
    void closedCannotGoAnywhere() {
        for (RequestStatus s : RequestStatus.values()) {
            assertFalse(RequestStatus.CLOSED.canTransitionTo(s));
        }
    }
}
