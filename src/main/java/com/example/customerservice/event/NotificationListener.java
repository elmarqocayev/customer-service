package com.example.customerservice.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Slf4j
@Component
public class NotificationListener {

    @Async("notificationExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onStatusChanged(ServiceRequestStatusChangedEvent event) {
        log.info("[{}] Bildiriş -> {} : müraciət #{} statusu {} -> {}",
                Thread.currentThread().getName(), event.customerEmail(),
                event.requestId(), event.oldStatus(), event.newStatus());
    }
}
