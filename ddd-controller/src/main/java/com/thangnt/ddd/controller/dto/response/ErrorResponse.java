package com.thangnt.ddd.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    String msg;
    HttpStatus status;
    String traceId;
    long timeStamp = System.currentTimeMillis();
}
