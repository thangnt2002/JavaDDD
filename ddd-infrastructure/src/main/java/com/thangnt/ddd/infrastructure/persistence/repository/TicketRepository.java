package com.thangnt.ddd.infrastructure.persistence.repository;

import com.thangnt.ddd.domain.model.entity.TicketDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketDetails, Long> {
}
