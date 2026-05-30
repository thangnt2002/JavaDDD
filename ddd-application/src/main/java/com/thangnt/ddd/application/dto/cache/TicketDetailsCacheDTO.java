package com.thangnt.ddd.application.dto.cache;

import com.thangnt.ddd.domain.model.entity.TicketDetails;
import lombok.Data;

@Data
public class TicketDetailsCacheDTO {
    Long version;
    TicketDetails ticketDetails;

    public TicketDetailsCacheDTO withClone(TicketDetails ticketDetails){
        this.ticketDetails = ticketDetails;
        return this;
    }

    public TicketDetailsCacheDTO withVersion(Long version){
        this.version = version;
        return this;
    }
}
