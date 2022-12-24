package com.github.voofai.camunda.ticket.history.db.repository;

import com.github.voofai.camunda.ticket.history.db.model.Deployment;
import com.github.voofai.camunda.ticket.history.db.model.PaginationContainerDTO;

public interface DeploymentRepositoryInt {
    PaginationContainerDTO<Deployment> getList(String deploymentKey, String maxResults, String firstResults);
    Deployment get(String deploymentId);

}
