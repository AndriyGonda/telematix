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
import org.telematix.models.sensor.Sensor;
import org.telematix.repositories.MessageRepository;
import org.telematix.repositories.SensorRepository;

@Service
public class MessageService {
    private static final String TIMESTAMP = "timestamp";
    private static final String MQTT_RECEIVED_TOPIC = "mqtt_receivedTopic";
    private static final String FAILED_TO_DECODE_GPS_JSON = "Unable to save message with type GPS_JSON. Decoding failed";
    private static final String FAILED_TO_DECODE_NUMBER = "Unable to save message with type NUMBER. Decoding failed";
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
                verifyAndSaveMessage(message, sensor, topicMessage);
            }
        });
    }

    private void verifyAndSaveMessage(Message<String> message, Sensor sensor, TopicMessage topicMessage) {
        switch (sensor.getSensorType()) {
            case GPS_JSON -> {
                try {
                    GeopositionDto position = parsePosition(message.getPayload());
                    if (position.getType().equals("location")) {
                        messageRepository.saveItem(topicMessage);
                    }
                } catch (JsonSyntaxException e) {
                    logger.warn(FAILED_TO_DECODE_GPS_JSON);
                }
            }
            case NUMBER -> {
                try {
                    logger.info("Received number {}", Float.parseFloat(message.getPayload()));
                    messageRepository.saveItem(topicMessage);
                } catch (NumberFormatException e) {
                    logger.warn(FAILED_TO_DECODE_NUMBER);
                }
            }
            case STRING -> {
                logger.info("Received string '{}'", message.getPayload());
                messageRepository.saveItem(topicMessage);
            }
        }
    }
}
