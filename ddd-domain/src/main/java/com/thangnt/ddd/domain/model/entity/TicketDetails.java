package com.thangnt.ddd.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "ticket_details",
        indexes = {
                @Index(name = "idx_end_time", columnList = "sale_end_time"),
                @Index(name = "idx_start_time", columnList = "sale_start_time"),
                @Index(name = "idx_status", columnList = "status")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "stock_initial", nullable = false)
    private Integer stockInitial;

    @Column(name = "stock_available", nullable = false)
    private Integer stockAvailable;

    @Column(name = "is_stock_prepared", nullable = false)
    private Boolean isStockPrepared;

    @Column(name = "price_original", nullable = false)
    private Long priceOriginal;

    @Column(name = "price_flash", nullable = false)
    private Long priceFlash;

    @Column(name = "sale_start_time", nullable = false)
    private LocalDateTime saleStartTime;

    @Column(name = "sale_end_time", nullable = false)
    private LocalDateTime saleEndTime;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}