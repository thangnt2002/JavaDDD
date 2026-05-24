package com.thangnt.ddd.infrastructure.persistence.repository;

import com.thangnt.ddd.domain.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTicketRepository extends JpaRepository<Ticket, Long> {
}
