package com.github.voofai.camunda.ticket.history.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service("databaseConnectionService")
@ConditionalOnProperty(prefix = "exporter", name = "type", havingValue = "h2")
public class H2ConnectionService {
    private static final Logger logger = LoggerFactory.getLogger(H2ConnectionService.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Connection connection;

    public void init() {
        try {
            this.connection = dataSource.getConnection();
        } catch (SQLException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    public void close() {
        // JDBC Template закроет соединение автоматически
    }
}
