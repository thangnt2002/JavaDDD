package com.thangnt.ddd.application.service.ticket;

import com.thangnt.ddd.domain.model.entity.Ticket;

public interface TicketDetailsAppService {
    Ticket getById(Long id);
}
