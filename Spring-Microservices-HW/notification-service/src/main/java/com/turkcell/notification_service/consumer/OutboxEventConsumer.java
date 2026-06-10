package com.turkcell.notification_service.consumer;

import com.turkcell.notification_service.event.TestEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxEventConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "outbox.product_db.public.outbox_events",
        groupId = "notification-group"
    )
    public void consume(String message) {
        try {
            log.info("[notification-service] Kafka mesajı alındı: {}", message);
            // Debezium CDC mesajı parse edilebilir, burada loglama yeterli
        } catch (Exception e) {
            log.error("Kafka mesajı işlenirken hata: {}", e.getMessage());
        }
    }
}
