package com.github.voofai.camunda.ticket.history.db;

import com.github.voofai.camunda.ticket.history.ExportRecordType;
import com.github.voofai.camunda.ticket.history.db.model.ExportRecord;

import java.util.concurrent.BlockingQueue;

public interface ExporterInterface {
    void init();
    void export(ExportRecordType exportRecordType, BlockingQueue<ExportRecord> records);
    void destroy();
}
