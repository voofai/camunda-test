package com.github.voofai.camunda.ticket.history.db.model;

import java.util.Date;

public class Process extends ExportRecord {
    private Date date;
    private String processDefinitionKey;
    private String processDefinitionId;
    private String businessKey;
    private String processInstanceId;
    private String superProcessInstanceId;
    private LifecycleType lifecycleType;
    private Date endDate;

    public Process(Date date, String processDefinitionKey, String processDefinitionId, String businessKey,
                   String processInstanceId, String superProcessInstanceId, LifecycleType lifecycleType, Date endDate) {
        this.date = date;
        this.processDefinitionKey = processDefinitionKey;
        this.processDefinitionId = processDefinitionId;
        this.businessKey = businessKey;
        this.processInstanceId = processInstanceId;
        this.superProcessInstanceId = superProcessInstanceId;
        this.lifecycleType = lifecycleType;
        this.endDate = endDate;
    }

    public Process() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getSuperProcessInstanceId() {
        return superProcessInstanceId;
    }

    public void setSuperProcessInstanceId(String superProcessInstanceId) {
        this.superProcessInstanceId = superProcessInstanceId;
    }

    public LifecycleType getLifecycleType() {
        return lifecycleType;
    }

    public void setLifecycleType(LifecycleType lifecycleType) {
        this.lifecycleType = lifecycleType;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Process{" +
                "processDefinitionKey='" + processDefinitionKey + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                ", businessKey='" + businessKey + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", superProcessInstanceId='" + superProcessInstanceId + '\'' +
                ", lifecycleType='" + lifecycleType + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
