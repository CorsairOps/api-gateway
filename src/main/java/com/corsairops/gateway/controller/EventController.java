package com.corsairops.gateway.controller;

import com.corsairops.shared.annotations.CommonWriteResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Operation(summary = "Publish an event to Kafka")
    @CommonWriteResponses
    @PostMapping("/publish/{topic}")
    @ResponseStatus(HttpStatus.CREATED)
    public void publishToTopic(@PathVariable String topic, @RequestBody String message) {
        log.info("Publishing event to topic: " + topic + " with value: " + message);
        kafkaTemplate.send("asset-location-updates", message);
    }
}