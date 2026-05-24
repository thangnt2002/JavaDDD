package com.thangnt.ddd.domain.repository;

import com.thangnt.ddd.domain.model.entity.Ticket;

public interface TicketRepository {
    Ticket findById(Long id);
}
