package com.github.voofai.camunda.ticket.history.db.model;

import java.util.List;

public class PaginationContainerDTO<T extends ExportRecord> {
    private Integer count;
    private List<T> list;

    public PaginationContainerDTO(Integer count, List<T> list) {
        this.count = count;
        this.list = list;
    }

    public Integer getCount() {
        return count;
    }

    public List<T> getList() {
        return list;
    }
}
