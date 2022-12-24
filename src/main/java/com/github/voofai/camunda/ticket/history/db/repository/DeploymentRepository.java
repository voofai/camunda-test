package com.github.voofai.camunda.ticket.history.db.repository;

import com.github.voofai.camunda.ticket.history.db.H2ConnectionService;
import com.github.voofai.camunda.ticket.history.db.model.Deployment;
import com.github.voofai.camunda.ticket.history.db.model.PaginationContainerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service("deploymentRepository")
@ConditionalOnProperty(prefix = "exporter", name = "type", havingValue = "db.migration")
public class DeploymentRepository implements DeploymentRepositoryInt {
    @Autowired
    private H2ConnectionService databaseConnectionService;

    private final DeploymentMapper deploymentMapper = new DeploymentMapper();

    @Override
    public PaginationContainerDTO<Deployment> getList(String deploymentKey, String maxResults, String firstResults) {
        return new PaginationContainerDTO<>(
                databaseConnectionService.getJdbcTemplate().queryForObject(
                        "SELECT COUNT(*) FROM DEPLOYMENT WHERE DEPLOYMENTKEY=?", Integer.class, deploymentKey),
                databaseConnectionService.getJdbcTemplate().query(
                        "SELECT * FROM DEPLOYMENT WHERE DEPLOYMENTKEY=? ORDER BY CREATED LIMIT ? OFFSET ?",
                        deploymentMapper, deploymentKey, maxResults, firstResults
                )
        );
    }

    @Override
    public Deployment get(String deploymentId) {
        return databaseConnectionService.getJdbcTemplate().queryForObject(
                "SELECT * FROM DEPLOYMENT WHERE DEPLOYMENTID=?", deploymentMapper, deploymentId);
    }

    class DeploymentMapper implements RowMapper<Deployment> {
        @Override
        public Deployment mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Deployment deployment = new Deployment();
            deployment.setDate(rs.getTimestamp("CREATED"));
            deployment.setDeploymentId(rs.getString("DEPLOYMENTID"));
            deployment.setXml(rs.getString("XML"));
            deployment.setFileName(rs.getString("FILENAME"));
            deployment.setDeploymentKey(rs.getString("DEPLOYMENTKEY"));
            return deployment;
        }
    }
}
