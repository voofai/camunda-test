package com.github.voofai.camunda.ticket.workflow;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SendTicket implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
      //  throw new NullPointerException("Can't send ticket to customer");
        var paymentNum = (String) execution.getVariable(Const.PAYMENT_NUM);
        log.info("Payment received, send ticket to client, payment num: " + paymentNum);
    }
}
