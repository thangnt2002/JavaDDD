package com.thangnt.ddd.application.service.ticket;

import com.thangnt.ddd.application.dto.response.TicketDetailsResponseDTO;
import com.thangnt.ddd.domain.model.entity.Ticket;

import java.util.List;

public interface TicketDetailsAppService {

    TicketDetailsResponseDTO getById(Long id, Long version);

    List<TicketDetailsResponseDTO> getAll();

    int orderTicket(Long ticketId, int quantity);
}
