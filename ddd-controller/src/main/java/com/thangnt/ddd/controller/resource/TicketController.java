package com.thangnt.ddd.controller.resource;

import com.thangnt.ddd.application.service.ticket.TicketDetailsAppService;
import com.thangnt.ddd.domain.model.entity.Ticket;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketController {

    TicketDetailsAppService ticketService;

    @GetMapping("/detail/{id}")
    public Ticket getById(@PathVariable("id") String id) {
        Long longId = Long.parseLong(id);
        return ticketService.getById(longId);
    }

    @GetMapping("/list")
    public List<Ticket> getAll() {
        return ticketService.getAll();
    }

}
