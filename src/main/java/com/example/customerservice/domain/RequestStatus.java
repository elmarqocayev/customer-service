package com.example.customerservice.domain;

/** Müraciətin həyat dövrü: OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED */
public enum RequestStatus {
    OPEN, IN_PROGRESS, RESOLVED, CLOSED;

    public boolean canTransitionTo(RequestStatus next) {
        return switch (this) {
            case OPEN -> next == IN_PROGRESS || next == CLOSED;
            case IN_PROGRESS -> next == RESOLVED || next == OPEN;
            case RESOLVED -> next == CLOSED || next == IN_PROGRESS;
            case CLOSED -> false;
        };
    }
}
