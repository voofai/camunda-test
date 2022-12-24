package com.github.voofai.camunda.ticket.history;

import com.github.voofai.camunda.ticket.history.db.ExporterInterface;
import com.github.voofai.camunda.ticket.history.db.model.*;
import com.github.voofai.camunda.ticket.history.db.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static com.github.voofai.camunda.ticket.history.ExportRecordType.*;
import static com.github.voofai.camunda.ticket.history.db.model.LifecycleType.*;

@Service
public class ExporterRegistryService {
    private static final Logger logger = LoggerFactory.getLogger(ExporterRegistryService.class);

    @Autowired
    @Qualifier("exporter")
    private ExporterInterface exporter;

    @Value("${exporter.enabled:false}")
    private boolean enabled;

    @Value("${exporter.batchSize}")
    private int batchSize = 500;

    @Value("${exporter.interval}")
    private long interval = 20;

    private boolean isRunning = false;

    private final Map<ExportRecordType, BlockingQueue<ExportRecord>> exportLog = new HashMap<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @PreDestroy
    public void destroy() {
        executor.shutdown();
        fullFlush();
        exporter.destroy();
    }

    @PostConstruct
    public void onStart() {
        if (enabled) {
            exporter.init();
            scheduleFlushBatchTask();
        }
    }

    public void registerStartProcess(Date dateTime, String processDefinitionKey, String processDefinitionId,
                                     String businessKey, String processInstanceId, String superProcessInstanceId) {
        final Process item = new Process(dateTime, processDefinitionKey, processDefinitionId,
                businessKey, processInstanceId, superProcessInstanceId, STARTED, null);
        export(PROCESS, item);
        logger.debug("registerStartProcess: {}", item);
    }

    public void registerEndProcess(Date dateTime, String processDefinitionKey, String processDefinitionId,
                                   String businessKey, String processInstanceId, String superProcessInstanceId) {
        final Process item = new Process(null, processDefinitionKey, processDefinitionId,
                businessKey, processInstanceId, superProcessInstanceId, ENDED, dateTime);
        export(PROCESS, item);
        logger.debug("registerEndProcess: {}", item);
    }

    public void registerActivity(Date datetime, String processInstanceId, String activityId,
                                 String activityInstanceId) {
        final Activity item = new Activity(datetime, processInstanceId, activityId, activityInstanceId, STARTED, null);
        export(ACTIVITY, item);
        logger.debug("registerActivity: {}", item);
    }

    public void registerActivityEnd(Date datetime, String processInstanceId, String activityId,
                                    String activityInstanceId) {
        final Activity item = new Activity(null, processInstanceId, activityId, activityInstanceId, ENDED,
                datetime);
        export(ACTIVITY, item);
        logger.debug("registerActivityEnd: {}", item);
    }

    public void registerVariable(Date timestamp, String processInstanceId, String variableName,
                                 String textValue, String serializerName) {
        final Variable item = new Variable(timestamp, processInstanceId, variableName, textValue, serializerName);
        export(VARIABLE, item);
        logger.debug("registerVariable: {}", item);
    }

    public void registerDeployment(Deployment deployment) {
        export(DEPLOYMENT, deployment);
        logger.debug("registerDeployment: {}", deployment);
    }

    public void registerModel(Model model) {
        export(MODEL, model);
        logger.debug("registerModel: {}", model);
    }

    // Накапливаем события в LinkedBlockingQueue по каждому типу событий
    public void export(ExportRecordType type, ExportRecord exportRecord) {
        if (enabled) {
            if (! this.exportLog.containsKey(type)) {
                this.exportLog.put(type, new LinkedBlockingQueue<>());
            }
            this.exportLog.get(type).add(exportRecord);

            // в случае переполнения пачки, запускаем запись в систему хранения
            if (this.exportLog.get(type).size() >= this.batchSize) {
                flush(type);
            }
        }
    }

    // Полная запись всех накопленных событий в систему хранения
    private void flushBatchTask() {
        fullFlush();
        scheduleFlushBatchTask();
    }
    private void fullFlush() {
        exportLog.entrySet()
            .forEach( x -> {
                if (x.getValue() != null && ! x.getValue().isEmpty()) {
                    flush(x.getKey());
                }
            });
    }

    // Создаем отложенное событие запуска по таймеру
    private void scheduleFlushBatchTask() {
        executor.schedule(this::flushBatchTask, interval, TimeUnit.SECONDS);
    }

    // Запускаем непосредственный слив данных в систему хранения
    private void flush(ExportRecordType type) {
        if (! isRunning) {
            isRunning = true;
            exporter.export(type, exportLog.get(type));
            isRunning = false;
        } else {
            logger.debug("Слишком большая нагрузка, экспорт не успевает");
        }
    }
}
