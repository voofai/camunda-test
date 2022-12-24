package com.github.voofai.camunda.ticket.history.db.model;

import java.util.Date;

public class Model extends ExportRecord {
    private Date date;
    private String deploymentKey;
    private String fileName;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDeploymentKey() {
        return deploymentKey;
    }

    public void setDeploymentKey(String deploymentKey) {
        this.deploymentKey = deploymentKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
