package com.thangnt.ddd.controller.resource;

import com.thangnt.ddd.application.dto.response.TicketResponseDTO;
import com.thangnt.ddd.application.service.ticket.TicketDetailsAppService;
import com.thangnt.ddd.controller.dto.response.ApiResponse;
import com.thangnt.ddd.domain.model.entity.Ticket;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketController {

    TicketDetailsAppService ticketService;

    @GetMapping("/detail/{id}")
    public ApiResponse<TicketResponseDTO> getById(
            @PathVariable("id") Long id,
            @RequestParam(name = "version", required = false) Long version) {
        TicketResponseDTO ticket = ticketService.getById(id, version);
        return ApiResponse.<TicketResponseDTO>builder()
                .success(true)
                .code(200)
                .result(ticket)
                .build();
    }

    @GetMapping("/list")
    public List<Ticket> getAll() {
        return ticketService.getAll();
    }
}
