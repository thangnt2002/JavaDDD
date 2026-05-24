package com.thangnt.ddd.domain.service;

import com.thangnt.ddd.domain.model.entity.Ticket;

public interface TicketDetailService {
    Ticket getById(Long id);
}
