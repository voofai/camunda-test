package com.github.voofai.camunda.ticket.domain;

import lombok.Value;

@Value
public class Ticket {
    int id;
    int row;
    int place;
    int movieId;
}
