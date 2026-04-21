package com.thangnt.ddd.application.service.ticket;

import com.thangnt.ddd.domain.model.entity.TicketDetails;

public interface TicketDetailsAppService {
    TicketDetails getById(Long id);
}
