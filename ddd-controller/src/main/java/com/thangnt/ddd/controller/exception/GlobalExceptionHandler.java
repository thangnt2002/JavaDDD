package com.thangnt.ddd.controller.exception;

import com.thangnt.ddd.controller.dto.response.ApiResponse;
import com.thangnt.ddd.controller.dto.response.ErrorResponse;
import com.thangnt.ddd.domain.common.DomainErrorCode;
import com.thangnt.ddd.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ApiResponse<ErrorResponse> domainExceptionHandler(DomainException ex) {
        HttpStatus status = getStatus(ex.getErrorCode());
        ErrorResponse error = ErrorResponse.builder()
                .status(status)
                .msg(ex.getMessage())
                .build();
        return ApiResponse.<ErrorResponse>builder()
                .success(false)
                .code(status.value())
                .result(error)
                .build();
    }

    private HttpStatus getStatus(DomainErrorCode errorCode) {
        switch (errorCode) {
            case TICKET_NOT_FOUND:
                return HttpStatus.NOT_FOUND;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

}
