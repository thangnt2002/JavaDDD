package com.thangnt.ddd.application.mapper;

import com.thangnt.ddd.application.dto.response.TicketResponseDTO;
import com.thangnt.ddd.domain.model.entity.Ticket;
import org.springframework.beans.BeanUtils;

public class TicketItemMapper {

    public static TicketResponseDTO toTicketDTO(Ticket ticket) {
        if(ticket == null) return null;
        TicketResponseDTO dto = new TicketResponseDTO();
        BeanUtils.copyProperties(ticket, dto);
        return dto;
    }

}
