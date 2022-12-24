package com.github.voofai.camunda.ticket.domain;

import com.github.voofai.camunda.ticket.workflow.Const;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketWindow {
    private final ProcessEngine processEngine;

    public void orderTicket(Ticket ticket) {
        processEngine.getRuntimeService().createProcessInstanceByKey("order-processing")
                .businessKey(String.valueOf(ticket.getId()))
                .setVariable(Const.TICKET, ticket)
                .execute();
    }

    public void paymentReceived(int ticketId, String paymentNum) {
        processEngine.getRuntimeService().createMessageCorrelation("PaymentReceived")
                .processInstanceBusinessKey(String.valueOf(ticketId))
                .setVariable(Const.PAYMENT_NUM, paymentNum)
                .correlate();
    }
}
