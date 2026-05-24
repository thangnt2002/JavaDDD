package com.thangnt.ddd.infrastructure.persistence;

import com.thangnt.ddd.domain.model.entity.Ticket;
import com.thangnt.ddd.domain.repository.TicketRepository;
import com.thangnt.ddd.infrastructure.persistence.repository.JpaTicketRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketRepositoryImpl implements TicketRepository {

    JpaTicketRepository jpaTicketRepository;

    @Override
    public Ticket findById(Long id) {
        return jpaTicketRepository.findById(id).get();
    }
}
