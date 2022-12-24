package com.github.voofai.camunda.ticket.history;

import org.camunda.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.camunda.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.camunda.bpm.engine.impl.history.event.HistoricVariableUpdateEventEntity;
import org.camunda.bpm.engine.impl.history.event.HistoryEvent;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class CustomHistoryHandler implements HistoryEventHandler {

    private final ExporterRegistryService exporterRegistryService;

    public CustomHistoryHandler(ExporterRegistryService exporterRegistryService) {
        this.exporterRegistryService = exporterRegistryService;
    }

    @Override
    public void handleEvent(HistoryEvent historyEvent) {
        if (historyEvent.getClass().equals(HistoricProcessInstanceEventEntity.class)) {
            final HistoricProcessInstanceEventEntity event = (HistoricProcessInstanceEventEntity) historyEvent;
            // логируем начало и окончание процесса
            if ("start".equals(event.getEventType())) {
                exporterRegistryService.registerStartProcess(event.getStartTime(), event.getProcessDefinitionKey(),
                        event.getProcessDefinitionId(), event.getBusinessKey(), event.getProcessInstanceId(),
                        event.getSuperProcessInstanceId());
                return;
            }
            if ("end".equals(event.getEventType())) {
                exporterRegistryService.registerEndProcess(event.getEndTime(), event.getProcessDefinitionKey(),
                        event.getProcessDefinitionId(), event.getBusinessKey(), event.getProcessInstanceId(),
                        event.getSuperProcessInstanceId());
                return;
            }
        }
        if (historyEvent.getClass().equals(HistoricActivityInstanceEventEntity.class)) {
            // логируем начало и окончание действий в процессе (тасок)
            final HistoricActivityInstanceEventEntity event = (HistoricActivityInstanceEventEntity) historyEvent;
            if ("start".equals(event.getEventType()) && event.getActivityInstanceState() == 0) {
                exporterRegistryService.registerActivity(
                event.getStartTime(),
                event.getProcessInstanceId(),
                event.getActivityId(),
                event.getActivityInstanceId());
                return;
            }
            if ("end".equals(event.getEventType())) {
                exporterRegistryService.registerActivityEnd(
                        event.getEndTime(),
                        event.getProcessInstanceId(),
                        event.getActivityId(),
                        event.getActivityInstanceId());
                return;
            }
        }
        if (historyEvent.getClass().equals(HistoricVariableUpdateEventEntity.class)) {
            // логируем изменение переменных
            final HistoricVariableUpdateEventEntity event = (HistoricVariableUpdateEventEntity) historyEvent;
            exporterRegistryService.registerVariable(event.getTimestamp(), event.getProcessInstanceId(),
                    event.getVariableName(), event.getTextValue(), event.getSerializerName());
        }
    }

    @Override
    public void handleEvents(List<HistoryEvent> historyEvents) {
        if (!historyEvents.isEmpty()) {
            historyEvents.forEach(this::handleEvent);
        }
    }
}
