package com.thangnt.ddd.application.service.ticket.impl;

import com.thangnt.ddd.application.service.ticket.TicketDetailsAppService;
import com.thangnt.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.thangnt.ddd.domain.model.entity.Ticket;
import com.thangnt.ddd.infrastructure.persistence.repository.JpaTicketRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketDetailAppService implements TicketDetailsAppService {

//    TicketDetailDomainService ticketDetailDomainService;

    TicketDetailCacheService ticketDetailCacheService;
    JpaTicketRepository ticketRepository;

    @Override
    public Ticket getById(Long id) {
        log.info("Find ticket {}", id);

        return ticketDetailCacheService.getTicketDefaultCache(id, System.currentTimeMillis());
    }

    @Override
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }
}
