package com.thangnt.ddd.domain.service;

import com.thangnt.ddd.domain.model.entity.Ticket;
import com.thangnt.ddd.domain.model.entity.TicketDetails;

public interface TicketDetailService {
    TicketDetails getById(Long id);
}
