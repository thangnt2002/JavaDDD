package com.thangnt.ddd.application.service.ticket.impl;

import com.thangnt.ddd.application.service.ticket.TicketDetailsAppService;
import com.thangnt.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.thangnt.ddd.domain.model.entity.Ticket;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketDetailAppService implements TicketDetailsAppService {

//    TicketDetailDomainService ticketDetailDomainService;

    TicketDetailCacheService ticketDetailCacheService;
    @Override
    public Ticket getById(Long id) {
        log.info("Find ticket {}", id);

        return ticketDetailCacheService.getTicketDefaultCache(id, System.currentTimeMillis());
    }
}
