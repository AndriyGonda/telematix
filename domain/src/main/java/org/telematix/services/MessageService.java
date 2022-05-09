package org.telematix.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.telematix.models.TopicMessage;
import org.telematix.repositories.MessageRepository;
import org.telematix.repositories.SensorRepository;

@Service
public class MessageService {
    private static final String TIMESTAMP = "timestamp";
    private static final String MQTT_RECEIVED_TOPIC = "mqtt_receivedTopic";
    private final SensorRepository sensorRepository;
    private final MessageRepository messageRepository;
    private final Logger logger = LoggerFactory.getLogger(MessageService.class);

    public MessageService(SensorRepository sensorRepository, MessageRepository messageRepository) {
        this.sensorRepository = sensorRepository;
        this.messageRepository = messageRepository;
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMqttMessage(Message<String> message) {
        MessageHeaders headers = message.getHeaders();
        String topic = (String) headers.get(MQTT_RECEIVED_TOPIC);
        Long timestamp = (Long) headers.get(TIMESTAMP);
        sensorRepository.getByTopic(topic).ifPresent(sensor -> {
            if (Objects.nonNull(timestamp)) {
                TopicMessage topicMessage = new TopicMessage();
                topicMessage.setRaw(message.getPayload());
                topicMessage.setSensorId(sensor.getId());
                topicMessage.setTimestamp(Timestamp.from(Instant.ofEpochMilli(timestamp)));
                messageRepository.saveItem(topicMessage);
                logger.info("Received message from topic '{}' at the timestamp '{}'", topic, timestamp);
            }
        });
    }
}
