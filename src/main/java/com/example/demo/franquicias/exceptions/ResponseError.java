package com.example.demo.franquicias.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseError {
    private String message;
}
