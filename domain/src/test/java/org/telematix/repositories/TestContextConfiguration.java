package org.telematix.repositories;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.logging.log4j.message.Message;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource(
        locations = "classpath:test.properties"
)
public class TestContextConfiguration {
    @Bean
    DataSource dataSource(
            @Value("${database.driver}")
                    String databaseDriver,
            @Value("${database.url}")
                    String datasourceUrl,

            @Value("${database.username}")
                    String username,

            @Value("${database.password}")
                    String password
    ) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(databaseDriver);
        config.setUsername(username);
        config.setPassword(password);
        config.setJdbcUrl(datasourceUrl);
        config.setAutoCommit(true);
        return new HikariDataSource(config);
    }

    @Bean
    Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .outOfOrder(true)
                .locations("classpath:db/migrations")
                .dataSource(dataSource)
                .load();
    }

    @Bean
    InitializingBean flywayMigrate(Flyway flyway) {
        return flyway::migrate;
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    UserRepository userRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new UserRepository(namedParameterJdbcTemplate);
    }

    @Bean
    DeviceRepository deviceRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new DeviceRepository(namedParameterJdbcTemplate);
    }

    @Bean
    SensorRepository sensorRepository(NamedParameterJdbcTemplate  namedParameterJdbcTemplate) {
        return new SensorRepository(namedParameterJdbcTemplate);
    }

    @Bean
    MessageRepository messageRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new MessageRepository(namedParameterJdbcTemplate);
    }
}
