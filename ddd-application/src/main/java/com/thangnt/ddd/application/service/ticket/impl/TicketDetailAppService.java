package com.thangnt.ddd.application.service.ticket.impl;

import com.thangnt.ddd.application.dto.response.TicketResponseDTO;
import com.thangnt.ddd.application.mapper.TicketItemMapper;
import com.thangnt.ddd.application.dto.cache.TicketCacheDTO;
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

    TicketDetailCacheService ticketDetailCacheService;
    JpaTicketRepository ticketRepository;

    @Override
    public TicketResponseDTO getById(Long id, Long version) {
        TicketCacheDTO ticketCacheDTO =  ticketDetailCacheService.getTicket(id, version);
        TicketResponseDTO dto =  TicketItemMapper.toTicketDTO(ticketCacheDTO.getTicket());
        dto.setVersion(ticketCacheDTO.getVersion());
        return dto;
    }

    @Override
    public List<Ticket> getAll() {
        return ticketRepository.findAll();
    }

    @Override
    public boolean orderTicket(Long ticketId) {
        return ticketDetailCacheService.orderTicket(ticketId);
    }
}
