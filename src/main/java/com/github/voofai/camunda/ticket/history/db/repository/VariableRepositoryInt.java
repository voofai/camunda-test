package com.github.voofai.camunda.ticket.history.db.repository;

import com.github.voofai.camunda.ticket.history.db.model.Variable;
import com.github.voofai.camunda.ticket.history.db.model.PaginationContainerDTO;

public interface VariableRepositoryInt {
    PaginationContainerDTO<Variable> getList(String processInstanceId, String maxResults, String firstResults);
}
