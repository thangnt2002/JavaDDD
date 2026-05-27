package com.thangnt.ddd.application.service.ticket;

import com.thangnt.ddd.application.dto.response.TicketResponseDTO;
import com.thangnt.ddd.domain.model.entity.Ticket;

import java.util.List;

public interface TicketDetailsAppService {

    TicketResponseDTO getById(Long id, Long version);

    List<Ticket> getAll();

    boolean orderTicket(Long ticketId);
}
