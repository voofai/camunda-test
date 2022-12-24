package com.github.voofai.camunda.ticket.history.db.repository;

import com.github.voofai.camunda.ticket.history.db.model.Activity;
import com.github.voofai.camunda.ticket.history.db.model.PaginationContainerDTO;

public interface ActivityRepositoryInt {
    PaginationContainerDTO<Activity> getList(String processInstanceId, String maxResults, String firstResults);
}
