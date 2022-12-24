package com.github.voofai.camunda.ticket.history.db.model;

import java.util.Date;

public class Variable extends ExportRecord {
    private Date date;
    private String processInstanceId;
    private String variableName;
    private String textValue;
    private String serializerName;

    public Variable() {
    }

    public Variable(Date date, String processInstanceId, String variableName, String textValue, String serializerName) {
        this.date = date;
        this.processInstanceId = processInstanceId;
        this.variableName = variableName;
        this.textValue = textValue;
        this.serializerName = serializerName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public String getSerializerName() {
        return serializerName;
    }

    public void setSerializerName(String serializerName) {
        this.serializerName = serializerName;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "processInstanceId='" + processInstanceId + '\'' +
                ", variableName='" + variableName + '\'' +
                ", textValue='" + textValue + '\'' +
                ", serializerName='" + serializerName + '\'' +
                '}';
    }
}
