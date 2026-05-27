package com.thangnt.ddd.application.dto.cache;

import com.thangnt.ddd.domain.model.entity.Ticket;
import lombok.Data;

@Data
public class TicketCacheDTO {
    Long version;
    Ticket ticket;

    public TicketCacheDTO withClone(Ticket ticket){
        this.ticket = ticket;
        return this;
    }

    public TicketCacheDTO withVersion(Long version){
        this.version = version;
        return this;
    }
}
