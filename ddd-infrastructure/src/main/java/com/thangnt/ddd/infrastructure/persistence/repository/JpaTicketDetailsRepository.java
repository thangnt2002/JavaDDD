package com.thangnt.ddd.infrastructure.persistence.repository;

import com.thangnt.ddd.domain.model.entity.TicketDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JpaTicketDetailsRepository extends JpaRepository<TicketDetails, Long> {

    @Transactional
    @Query("SELECT t.stockAvailable FROM TicketDetails t WHERE t.id = :ticketId")
    int getStockAvailable(@Param("ticketId") long ticketId);

    @Modifying
    @Transactional
    @Query("UPDATE TicketDetails t SET t.updatedAt = CURRENT_TIMESTAMP() " +
            ", t.stockAvailable = t.stockAvailable - :quantity " +
            "WHERE t.id = :id AND t.stockAvailable > :quantity")
    int decreaseStock(@Param("ticketId") long ticketId, @Param("quantity") int quantity);

    @Modifying
    @Transactional
    @Query("UPDATE TicketDetails t SET t.updatedAt = CURRENT_TIMESTAMP() " +
            ", t.stockAvailable = :currentStockAvailable - :quantity " +
            "WHERE t.id = :id AND t.stockAvailable = :currentStockAvailable")
    int decreaseStockCAS(@Param("ticketId") long ticketId, @Param("currentStockAvailable") int currentStockAvailable, @Param("quantity") int quantity);
}
