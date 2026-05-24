package com.thangnt.ddd.domain.service.impl;

import com.thangnt.ddd.domain.model.entity.Ticket;
import com.thangnt.ddd.domain.repository.TicketRepository;
import com.thangnt.ddd.domain.service.TicketDetailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketDetailServiceImpl implements TicketDetailService {

    TicketRepository ticketRepository;

    @Override
    public Ticket getById(Long id) {
        return ticketRepository.findById(id);
    }
}
