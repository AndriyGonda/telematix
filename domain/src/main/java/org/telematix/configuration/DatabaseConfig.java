package org.telematix.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@PropertySource("classpath:application.properties")
public class DatabaseConfig {

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
}
