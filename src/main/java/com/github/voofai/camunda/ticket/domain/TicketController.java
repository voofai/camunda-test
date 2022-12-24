package com.github.voofai.camunda.ticket.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TicketController {
    private final TicketWindow ticketWindow;

    @PostMapping("/order")
    public void orderTicket(Ticket ticket) {
        ticketWindow.orderTicket(ticket);
    }

    @PostMapping("/pay")
    public void payForTicket(int ticketId, String paymentNum) {
        ticketWindow.paymentReceived(ticketId, paymentNum);
    }
}