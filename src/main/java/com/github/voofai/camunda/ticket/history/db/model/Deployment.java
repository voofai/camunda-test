package com.github.voofai.camunda.ticket.history.db.model;

import java.util.Date;

public class Deployment extends ExportRecord {
    private Date date;
    private String deploymentId;
    private String xml;
    private String fileName;
    private String deploymentKey;

    public Deployment() {
    }

    public Deployment(Date date, String deploymentId, String xml, String fileName, String deploymentKey) {
        this.date = date;
        this.deploymentId = deploymentId;
        this.xml = xml;
        this.fileName = fileName;
        this.deploymentKey = deploymentKey;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDeploymentKey() {
        return deploymentKey;
    }

    public void setDeploymentKey(String deploymentKey) {
        this.deploymentKey = deploymentKey;
    }


}
