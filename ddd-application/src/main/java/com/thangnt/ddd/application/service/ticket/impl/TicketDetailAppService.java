package com.thangnt.ddd.application.service.ticket.impl;

import com.thangnt.ddd.application.dto.response.TicketDetailsResponseDTO;
import com.thangnt.ddd.application.mapper.TicketDetailsMapper;
import com.thangnt.ddd.application.dto.cache.TicketDetailsCacheDTO;
import com.thangnt.ddd.application.service.ticket.TicketDetailsAppService;
import com.thangnt.ddd.application.service.ticket.cache.TicketDetailsCacheService;
import com.thangnt.ddd.domain.model.entity.TicketDetails;
import com.thangnt.ddd.infrastructure.persistence.repository.JpaTicketDetailsRepository;
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

    TicketDetailsCacheService ticketDetailsCacheService;
    JpaTicketDetailsRepository ticketDetailsRepository;

    @Override
    public TicketDetailsResponseDTO getById(Long id, Long version) {
        TicketDetailsCacheDTO ticketDetailsCacheDTO =  ticketDetailsCacheService.getTicketDetails(id, version);
        TicketDetailsResponseDTO dto =  TicketDetailsMapper.toTicketDetailsDTO(ticketDetailsCacheDTO.getTicketDetails());
        dto.setVersion(ticketDetailsCacheDTO.getVersion());
        return dto;
    }

    @Override
    public List<TicketDetailsResponseDTO> getAll() {
        List<TicketDetails> ticketDetailsList = ticketDetailsRepository.findAll();
        return ticketDetailsList
                .stream()
                .map(TicketDetailsMapper::toTicketDetailsDTO)
                .toList();
    }

    @Override
    public int orderTicket(Long ticketId, int quantity) {
        return ticketDetailsCacheService.orderTicket(ticketId, quantity);
    }
}
