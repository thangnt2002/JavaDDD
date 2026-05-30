package com.thangnt.ddd.application.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDetailsResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private Integer stockInitial;
    private Integer stockAvailable;
    private Boolean isStockPrepared;
    private Long priceOriginal;
    private Long priceFlash;
    private LocalDateTime saleStartTime;
    private LocalDateTime saleEndTime;
    private Long activityId;
    private Long version;
}
