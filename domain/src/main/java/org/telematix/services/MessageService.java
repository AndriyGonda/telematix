package org.telematix.services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.telematix.dto.GeopositionDto;
import org.telematix.models.TopicMessage;
import org.telematix.models.sensor.SensorType;
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

    private GeopositionDto parsePosition(String message) {
        Gson gson = new Gson();
        return gson.fromJson(message, GeopositionDto.class);
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMqttMessage(Message<String> message) {
        MessageHeaders headers = message.getHeaders();
        String topic = (String) headers.get(MQTT_RECEIVED_TOPIC);
        Long timestamp = (Long) headers.get(TIMESTAMP);
        sensorRepository.getByTopic(topic).ifPresent(sensor -> {
            if (Objects.nonNull(timestamp)) {
                logger.info("Received message from topic '{}' at the timestamp '{}'", topic, timestamp);
                TopicMessage topicMessage = new TopicMessage();
                topicMessage.setRaw(message.getPayload());
                topicMessage.setSensorId(sensor.getId());
                topicMessage.setTimestamp(Timestamp.from(Instant.ofEpochMilli(timestamp)));
                if (!sensor.getSensorType().equals(SensorType.GPS_JSON)) {
                    messageRepository.saveItem(topicMessage);
                } else {
                    try {
                        parsePosition(message.getPayload());
                        messageRepository.saveItem(topicMessage);
                    } catch (JsonSyntaxException e) {
                        logger.warn("Unable to save message with type GPS_JSON. Decoding failed");
                    }
                }
            }
        });
    }
}
