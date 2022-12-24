package com.github.voofai.camunda.ticket.history.db.repository;

import com.github.voofai.camunda.ticket.history.db.model.Process;
import com.github.voofai.camunda.ticket.history.db.model.PaginationContainerDTO;

public interface ProcessRepositoryInt {
    PaginationContainerDTO<Process> getList(boolean isActive, String maxResults, String firstResults);
    Process get(String processInstanceId);
}
