package com.github.voofai.camunda.ticket.workflow;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CancelOrder implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Cancel order");
    }
}
