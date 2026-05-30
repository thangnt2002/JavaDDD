package com.thangnt.ddd.application.mapper;

import com.thangnt.ddd.application.dto.response.TicketDetailsResponseDTO;
import com.thangnt.ddd.domain.model.entity.Ticket;
import com.thangnt.ddd.domain.model.entity.TicketDetails;
import org.springframework.beans.BeanUtils;

public class TicketDetailsMapper {

    public static TicketDetailsResponseDTO toTicketDetailsDTO(TicketDetails ticket) {
        if(ticket == null) return null;
        TicketDetailsResponseDTO dto = new TicketDetailsResponseDTO();
        BeanUtils.copyProperties(ticket, dto);
        return dto;
    }

}
