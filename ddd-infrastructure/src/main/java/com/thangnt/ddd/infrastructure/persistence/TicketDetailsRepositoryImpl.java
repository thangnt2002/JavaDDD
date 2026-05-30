package com.thangnt.ddd.infrastructure.persistence;

import com.thangnt.ddd.domain.model.entity.TicketDetails;
import com.thangnt.ddd.domain.repository.TicketDetailsRepository;
import com.thangnt.ddd.infrastructure.persistence.repository.JpaTicketDetailsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketDetailsRepositoryImpl implements TicketDetailsRepository {

    JpaTicketDetailsRepository ticketDetailsRepository;

    @Override
    public TicketDetails findById(long ticketId) {
        return ticketDetailsRepository.findById(ticketId).get();
    }

    @Override
    public int decreaseStockAvailable(long ticketId, int stockAvailable) {
        return ticketDetailsRepository.decreaseStock(ticketId, stockAvailable);
    }

    @Override
    public int decreaseStockAvailableCAS(long ticketId, int stockAvailable, int quantity) {
        return 0;
    }
}
