package com.thangnt.ddd.domain.exception;

import com.thangnt.ddd.domain.common.DomainErrorCode;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    DomainErrorCode errorCode;
    String msg;

    public DomainException(DomainErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.msg = message;
    }
}
