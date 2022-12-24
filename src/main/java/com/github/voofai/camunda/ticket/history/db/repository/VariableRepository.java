package com.github.voofai.camunda.ticket.history.db.repository;

import com.github.voofai.camunda.ticket.history.db.H2ConnectionService;
import com.github.voofai.camunda.ticket.history.db.model.PaginationContainerDTO;
import com.github.voofai.camunda.ticket.history.db.model.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service("variableRepository")
@ConditionalOnProperty(prefix = "exporter", name = "type", havingValue = "db.migration")
public class VariableRepository implements VariableRepositoryInt {
    @Autowired
    private H2ConnectionService databaseConnectionService;

    private final VariableMapper variableMapper = new VariableMapper();

    @Override
    public PaginationContainerDTO<Variable> getList(String processInstanceId, String maxResults, String firstResults) {
        return new PaginationContainerDTO<>(
                databaseConnectionService.getJdbcTemplate().queryForObject(
                        "SELECT COUNT(*) FROM VARIABLE WHERE PROCESSINSTANCEID=?", Integer.class,
                        processInstanceId),
                databaseConnectionService.getJdbcTemplate().query(
                        "SELECT * FROM VARIABLE WHERE PROCESSINSTANCEID=? ORDER BY created LIMIT ? OFFSET ?",
                        variableMapper, processInstanceId, maxResults, firstResults
                )
        );
    }

    class VariableMapper implements RowMapper<Variable> {
        @Override
        public Variable mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Variable variable = new Variable();
            variable.setDate(rs.getTimestamp("CREATED"));
            variable.setProcessInstanceId(rs.getString("PROCESSINSTANCEID"));
            variable.setVariableName(rs.getString("VARIABLENAME"));
            variable.setTextValue(rs.getString("TEXTVALUE"));
            variable.setSerializerName(rs.getString("SERIALIZERNAME"));
            return variable;
        }
    }
}
