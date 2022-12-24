package com.github.voofai.camunda.ticket.history.db.repository;


import com.github.voofai.camunda.ticket.history.db.model.Model;
import com.github.voofai.camunda.ticket.history.db.model.PaginationContainerDTO;

public interface ModelRepositoryInt {
    PaginationContainerDTO<Model> getList(String maxResults, String firstResults);
}
