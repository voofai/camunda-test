package com.github.voofai.camunda.ticket.workflow;

public interface Const {
    String TICKET = "ticket";
    String IS_PLACES_ARE_FREE = "isPlacesAreFree";
    String PAYMENT_NUM = "paymentNum";

    enum Order {
        ORDER_1;
    }

    enum Event{
        PAYMENT_RECEIVED
    }
}
