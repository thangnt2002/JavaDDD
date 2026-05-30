package com.thangnt.ddd.controller.resource;

import com.thangnt.ddd.application.dto.response.TicketDetailsResponseDTO;
import com.thangnt.ddd.application.service.ticket.TicketDetailsAppService;
import com.thangnt.ddd.controller.dto.response.ApiResponse;
import com.thangnt.ddd.domain.model.entity.Ticket;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket-details")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketDetailsController {

    TicketDetailsAppService ticketDetailsService;

    @GetMapping("/detail/{id}")
    public ApiResponse<TicketDetailsResponseDTO> getById(
            @PathVariable("id") Long id,
            @RequestParam(name = "version", required = false) Long version) {
        TicketDetailsResponseDTO ticket = ticketDetailsService.getById(id, version);
        return ApiResponse.<TicketDetailsResponseDTO>builder()
                .success(true)
                .code(200)
                .result(ticket)
                .build();
    }

    @GetMapping("/list")
    public ApiResponse<List<TicketDetailsResponseDTO>> getAll() {
        List<TicketDetailsResponseDTO> response =  ticketDetailsService.getAll();
        return ApiResponse.<List<TicketDetailsResponseDTO>>builder()
                .success(true)
                .code(200)
                .result(response)
                .build();
    }

    @GetMapping("/order/{ticketId}/{quantity}")
    public ApiResponse<Integer> orderTicket(@PathVariable long ticketId, @PathVariable int quantity){
        int response = ticketDetailsService.orderTicket(ticketId, quantity);
        return ApiResponse.<Integer>builder()
                .success(true)
                .code(200)
                .result(response)
                .build();
    }


}
