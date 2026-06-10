package com.turkcell.product_service.controller;

import com.turkcell.product_service.entity.OutboxEvent;
import com.turkcell.product_service.entity.OutboxStatus;
import com.turkcell.product_service.event.TestEvent;
import com.turkcell.product_service.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductsController {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/hello")
    public String hello() {
        return "Hello product-service";
    }

    @GetMapping("/test")
    public String test(@RequestParam String message) {
        // ASLA!
        UUID id = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        var event = new TestEvent(eventId, message, id);
        // streamBridge.send("testEvent-out-0", event);

        // KAFKAYA bir event gidecekse, önce kayıt altına alınacak.
        // Outbox -> XEvent, XTarihi, XTopic, XPayload

        // Daha sonra bir mekanizma bu kayıtları okuyacak ve kafkaya gönderecek.
        // POLLING ->
        // Debezium gibi bir mekanizma

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setId(eventId);
        outboxEvent.setAggregateType("Product");
        outboxEvent.setAggregateId(id.toString());
        outboxEvent.setEventType("TestEvent");
        outboxEvent.setPayload(toJson(event));
        outboxEvent.setStatus(OutboxStatus.PENDING);
        outboxEvent.setCreatedAt(Instant.now());

        outboxRepository.save(outboxEvent);

        return "Event kaydedildi: " + eventId;
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON dönüşüm hatası", e);
        }
    }
}
