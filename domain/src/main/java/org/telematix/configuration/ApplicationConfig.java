package org.telematix.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DatabaseConfig.class, MqttConfig.class})
public class ApplicationConfig {
}
