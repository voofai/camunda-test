package com.github.voofai.camunda.ticket.history.db.repository;

import com.github.voofai.camunda.ticket.history.db.H2ConnectionService;
import com.github.voofai.camunda.ticket.history.db.model.Activity;
import com.github.voofai.camunda.ticket.history.db.model.LifecycleType;
import com.github.voofai.camunda.ticket.history.db.model.PaginationContainerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service("activityRepository")
@ConditionalOnProperty(prefix = "exporter", name = "type", havingValue = "db.migration")
public class ActivityRepository implements ActivityRepositoryInt {
    @Autowired
    private H2ConnectionService databaseConnectionService;

    private final ActivityMapper activityMapper = new ActivityMapper();

    @Override
    public PaginationContainerDTO<Activity> getList(String processInstanceId, String maxResults, String firstResults) {
        return new PaginationContainerDTO<>(
                databaseConnectionService.getJdbcTemplate().queryForObject(
                        "SELECT COUNT(*) FROM (" + getQuery() + ")", Integer.class, processInstanceId),
                databaseConnectionService.getJdbcTemplate().query(
                        getQuery() + " ORDER BY CREATED LIMIT ? OFFSET ?",
                        activityMapper, processInstanceId, maxResults, firstResults
                )
        );
    }

    private String getQuery() {
        return "select p1.CREATED, p1.PROCESSINSTANCEID, p1.ACTIVITYID, p1.ACTIVITYINSTANCEID,\n"
                + "       casewhen(not p2.LIFECYCLETYPE is null, p2.LIFECYCLETYPE, p1.LIFECYCLETYPE) as "
                + "LIFECYCLETYPE,\n"
                + "       p2.ENDDATE\n"
                + "from ACTIVITY p1\n"
                + "    left join ACTIVITY p2 on p1.ACTIVITYINSTANCEID=p2.ACTIVITYINSTANCEID and p2"
                + ".LIFECYCLETYPE='ENDED'\n"
                + "where p1.LIFECYCLETYPE='STARTED' and p1.PROCESSINSTANCEID=? ";
    }

    class ActivityMapper implements RowMapper<Activity> {
        @Override
        public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Activity activity = new Activity();
            activity.setDate(rs.getTimestamp("CREATED"));
            activity.setProcessInstanceId(rs.getString("PROCESSINSTANCEID"));
            activity.setActivityId(rs.getString("ACTIVITYID"));
            activity.setActivityInstanceId(rs.getString("ACTIVITYINSTANCEID"));
            activity.setLifecycleType(LifecycleType.valueOf(rs.getString("LIFECYCLETYPE")));
            activity.setEndDate(rs.getTimestamp("ENDDATE"));
            return activity;
        }
    }
}
