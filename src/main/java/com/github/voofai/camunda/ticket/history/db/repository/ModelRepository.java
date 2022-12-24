package com.github.voofai.camunda.ticket.history.db.repository;

import com.github.voofai.camunda.ticket.history.db.H2ConnectionService;
import com.github.voofai.camunda.ticket.history.db.model.Model;
import com.github.voofai.camunda.ticket.history.db.model.PaginationContainerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service("modelRepository")
@ConditionalOnProperty(prefix = "exporter", name = "type", havingValue = "db.migration")
public class ModelRepository implements ModelRepositoryInt {
    @Autowired
    private H2ConnectionService databaseConnectionService;

    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public PaginationContainerDTO<Model> getList(String maxResults, String firstResults) {
        return new PaginationContainerDTO<>(
                databaseConnectionService.getJdbcTemplate().queryForObject(
                        "SELECT COUNT(*) FROM MODEL ", Integer.class),
                databaseConnectionService.getJdbcTemplate().query(
                        "SELECT * FROM MODEL ORDER BY created LIMIT ? OFFSET ?",
                        modelMapper, maxResults, firstResults
                )
        );
    }

    class ModelMapper implements RowMapper<Model> {
        @Override
        public Model mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Model model = new Model();
            model.setDate(rs.getTimestamp("CREATED"));
            model.setDeploymentKey(rs.getString("DEPLOYMENTKEY"));
            model.setFileName(rs.getString("FILENAME"));
            return model;
        }
    }
}
