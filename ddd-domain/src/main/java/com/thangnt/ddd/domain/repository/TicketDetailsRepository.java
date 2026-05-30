package com.thangnt.ddd.domain.repository;

import com.thangnt.ddd.domain.model.entity.TicketDetails;

public interface TicketDetailsRepository {
    TicketDetails findById(long ticketId);

    int decreaseStockAvailable(long ticketId, int stockAvailable);
    int decreaseStockAvailableCAS(long ticketId, int stockAvailable, int quantity);
}
