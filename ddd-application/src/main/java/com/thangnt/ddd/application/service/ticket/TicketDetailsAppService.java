package com.thangnt.ddd.application.service.ticket;

import com.thangnt.ddd.domain.model.entity.Ticket;

import java.util.List;

public interface TicketDetailsAppService {
    Ticket getById(Long id);

    List<Ticket> getAll();
}
