package com.github.voofai.camunda.ticket.history.db.repository;

import com.github.voofai.camunda.ticket.history.db.H2ConnectionService;
import com.github.voofai.camunda.ticket.history.db.model.LifecycleType;
import com.github.voofai.camunda.ticket.history.db.model.Process;
import com.github.voofai.camunda.ticket.history.db.model.PaginationContainerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service("processRepository")
@ConditionalOnProperty(prefix = "exporter", name = "type", havingValue = "db.migration")
public class ProcessRepository implements ProcessRepositoryInt {
    @Autowired
    private H2ConnectionService databaseConnectionService;

    private final ProcessMapper processMapper = new ProcessMapper();

    @Override
    public PaginationContainerDTO<Process> getList(boolean isActive, String maxResults, String firstResults) {
        return new PaginationContainerDTO<>(
                databaseConnectionService.getJdbcTemplate().queryForObject(
                        "SELECT COUNT(*) FROM (" + query(isActive) + ")"
                        , Integer.class),
                databaseConnectionService.getJdbcTemplate().query(
                        query(isActive)
                                + " ORDER BY CREATED LIMIT ? OFFSET ?",
                        processMapper, maxResults, firstResults
                )
        );
    }

    @Override
    public Process get(String processInstanceId) {
        return databaseConnectionService.getJdbcTemplate().queryForObject(
                "SELECT * FROM PROCESS WHERE PROCESSINSTANCEID=?", processMapper);
    }

    private String query(Boolean isActive) {
        return "select p1.CREATED, p1.PROCESSDEFINITIONKEY, p1.PROCESSDEFINITIONID, p1.BUSINESSKEY, p1"
                + ".PROCESSINSTANCEID,\n"
                + "       p1.SUPERPROCESSINSTANCEID,\n"
                + "       casewhen(not p2.LIFECYCLETYPE is null, p2.LIFECYCLETYPE, p1.LIFECYCLETYPE) as "
                + "LIFECYCLETYPE,\n"
                + "       p2.ENDDATE\n"
                + "from PROCESS p1\n"
                + "    left join PROCESS p2 on p1.PROCESSINSTANCEID=p2.PROCESSINSTANCEID and p2.LIFECYCLETYPE='ENDED'\n"
                + "where p1.LIFECYCLETYPE='STARTED' "
                + (isActive ? "and p2.LIFECYCLETYPE is null " : "and p2.LIFECYCLETYPE='ENDED' ");
    }

    class ProcessMapper implements RowMapper<Process> {
        @Override
        public Process mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Process process = new Process();
            process.setDate(rs.getTimestamp("CREATED"));
            process.setProcessDefinitionKey(rs.getString("PROCESSDEFINITIONKEY"));
            process.setProcessDefinitionId(rs.getString("PROCESSDEFINITIONID"));
            process.setBusinessKey(rs.getString("BUSINESSKEY"));
            process.setProcessInstanceId(rs.getString("PROCESSINSTANCEID"));
            process.setSuperProcessInstanceId(rs.getString("SUPERPROCESSINSTANCEID"));
            process.setLifecycleType(LifecycleType.valueOf(rs.getString("LIFECYCLETYPE")));
            process.setEndDate(rs.getTimestamp("ENDDATE"));
            return process;
        }
    }
}
