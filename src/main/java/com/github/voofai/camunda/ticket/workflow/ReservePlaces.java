package com.github.voofai.camunda.ticket.workflow;

import com.github.voofai.camunda.ticket.domain.Ticket;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReservePlaces implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        var ticket = (Ticket) execution.getVariable(Const.TICKET);
        // Если id билета четное - считаем, что места свободны, иначе их кто-то уже зарезервировал
        if (ticket.getId() % 2 == 0) {
            execution.setVariable(Const.IS_PLACES_ARE_FREE, Boolean.TRUE);
        } else {
            execution.setVariable(Const.IS_PLACES_ARE_FREE, Boolean.FALSE);
        }
        log.info("Reserve places: " + ticket);
    }
}
