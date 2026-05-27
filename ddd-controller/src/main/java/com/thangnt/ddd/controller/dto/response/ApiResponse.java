package com.thangnt.ddd.controller.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    boolean success;
    String message;
    Integer code;
    long timeStamp = System.currentTimeMillis();
    T result;
}
