package com.github.voofai.camunda.ticket.history.db;

import com.github.voofai.camunda.ticket.history.ExportRecordType;
import com.github.voofai.camunda.ticket.history.db.model.*;
import com.github.voofai.camunda.ticket.history.db.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Service("exporter")
@ConditionalOnProperty(prefix = "exporter", name = "type", havingValue = "h2")
public class H2Exporter implements ExporterInterface {
    private static final Logger logger = LoggerFactory.getLogger(H2Exporter.class);

    @Autowired
    private H2ConnectionService databaseConnectionService;

    @Override
    public void init() {
        databaseConnectionService.init();
    }

    @Override
    public void export(ExportRecordType exportRecordType, BlockingQueue<ExportRecord> records) {
        String queryString;
        String valuesString;
        final List parameters = new ArrayList<>();
        int itemsAdded = 0;
        // определяем команду sql для вставки или изменений данных и набор параметров
        switch (exportRecordType) {
            case MODEL:
                queryString = "merge into MODEL (CREATED,DEPLOYMENTKEY,FILENAME) KEY(DEPLOYMENTKEY) values ";
                valuesString = "(?,?,?)";
                break;
            case DEPLOYMENT:
                queryString = "insert into DEPLOYMENT (CREATED, DEPLOYMENTID, XML,FILENAME,DEPLOYMENTKEY) values ";
                valuesString = "(?,?,?,?,?)";
                break;
            case PROCESS:
                queryString = "insert into PROCESS "
                        + "(CREATED, PROCESSDEFINITIONKEY, PROCESSDEFINITIONID, BUSINESSKEY, PROCESSINSTANCEID, "
                        + "SUPERPROCESSINSTANCEID, LIFECYCLETYPE,ENDDATE) values ";
                valuesString = "(?,?,?,?,?,?,?,?)";
                break;
            case ACTIVITY:
                queryString = "insert into ACTIVITY "
                        + "(CREATED, PROCESSINSTANCEID, ACTIVITYID, ACTIVITYINSTANCEID, LIFECYCLETYPE,ENDDATE) "
                        + "values ";
                valuesString = "(?,?,?,?,?,?)";
                break;
            case VARIABLE:
                queryString = "insert into VARIABLE (CREATED, PROCESSINSTANCEID, VARIABLENAME, TEXTVALUE, "
                        + "SERIALIZERNAME) values ";
                valuesString = "(?,?,?,?,?)";
                break;
            default:
                throw new UnsupportedOperationException("ExportType doesnt implemented: " + exportRecordType);
        }
        // вычитываем данные из LinkedBlockingQueue и создаем один большой запрос с параметрами
        while (! records.isEmpty()) {
            final ExportRecord recordElement = records.poll();
            itemsAdded++;
            switch (exportRecordType) {
                case MODEL:
                    Model model = (Model) recordElement;
                    parameters.add(model.getDate());
                    parameters.add(model.getDeploymentKey());
                    parameters.add(model.getFileName());
                    break;
                case DEPLOYMENT:
                    Deployment deployment = (Deployment) recordElement;
                    parameters.add(deployment.getDate());
                    parameters.add(deployment.getDeploymentId());
                    parameters.add(deployment.getXml());
                    parameters.add(deployment.getFileName());
                    parameters.add(deployment.getDeploymentKey());
                    break;
                case PROCESS:
                    Process process = (Process) recordElement;
                    parameters.add(process.getDate());
                    parameters.add(process.getProcessDefinitionKey());
                    parameters.add(process.getProcessDefinitionId());
                    parameters.add(process.getBusinessKey());
                    parameters.add(process.getProcessInstanceId());
                    parameters.add(process.getSuperProcessInstanceId());
                    parameters.add(process.getLifecycleType().name());
                    parameters.add(process.getEndDate());
                    break;
                case ACTIVITY:
                    Activity activity = (Activity) recordElement;
                    parameters.add(activity.getDate());
                    parameters.add(activity.getProcessInstanceId());
                    parameters.add(activity.getActivityId());
                    parameters.add(activity.getActivityInstanceId());
                    parameters.add(activity.getLifecycleType().name());
                    parameters.add(activity.getEndDate());
                    break;
                case VARIABLE:
                    Variable variable = (Variable) recordElement;
                    parameters.add(variable.getDate());
                    parameters.add(variable.getProcessInstanceId());
                    parameters.add(variable.getVariableName());
                    parameters.add(variable.getTextValue());
                    parameters.add(variable.getSerializerName());
                    break;
                default:
            }
        }
        if (itemsAdded > 0 && ! parameters.isEmpty()) {
            String query = queryString + (valuesString + ",").repeat(itemsAdded);
            query = query.substring(0, query.length() - 1);
            execute(query, parameters);
        }
    }

    private void execute(String query, List<Object> parameters) {
        try (PreparedStatement statement = databaseConnectionService.getConnection()
                .prepareStatement(query)) {
            for (int i=0; i<parameters.size(); i++) {
                if (parameters.get(i) instanceof String) {
                    statement.setString(i+1, String.valueOf(parameters.get(i)));
                }
                if (parameters.get(i) instanceof Long) {
                    statement.setLong(i+1, (Long) parameters.get(i));
                }
                if (parameters.get(i) instanceof Integer) {
                    statement.setInt(i+1, (Integer) parameters.get(i));
                }
                if (parameters.get(i) instanceof Double) {
                    statement.setDouble(i+1, (Double) parameters.get(i));
                }
                if (parameters.get(i) instanceof Date) {
                    statement.setTime(i+1, convertToTime((Date) parameters.get(i)));
                }
                if (parameters.get(i) == null) {
                    statement.setString(i+1, null);
                }
            }
            // выполняем запрос
            statement.execute();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private java.sql.Time convertToTime(Date date) {
        return new java.sql.Time(date.getTime());
    }

    @Override
    public void destroy() {
        databaseConnectionService.close();
    }

}
