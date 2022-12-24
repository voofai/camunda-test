package com.github.voofai.camunda.ticket.history.db.model;

import java.util.Date;

public class Activity extends ExportRecord {
    private Date date;
    private String processInstanceId;
    private String activityId;
    private String activityInstanceId;
    private LifecycleType lifecycleType;
    private Date endDate;

    public Activity() {
    }

    public Activity(Date date, String processInstanceId, String activityId, String activityInstanceId,
                    LifecycleType lifecycleType, Date endDate) {
        this.date = date;
        this.processInstanceId = processInstanceId;
        this.activityId = activityId;
        this.activityInstanceId = activityInstanceId;
        this.lifecycleType = lifecycleType;
        this.endDate = endDate;
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityInstanceId() {
        return activityInstanceId;
    }

    public void setActivityInstanceId(String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
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
        return "Activity{" +
                "processInstanceId='" + processInstanceId + '\'' +
                ", activityId='" + activityId + '\'' +
                ", activityInstanceId='" + activityInstanceId + '\'' +
                ", lifecycleType=" + lifecycleType +
                '}';
    }
}
