package com.tienda.backend.exceptions;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import com.tienda.backend.models.dtos.ErrorDto;

public class NotFoundException extends TiendaException {
    private static final long serialVersionUID = 1L;

    /**
     * @param code
     * @param responseCode
     * @param message
     * @param errorList
     */
    public NotFoundException(String code, String message, ErrorDto data) {
        super(code, HttpStatus.NOT_FOUND.value(), message, Arrays.asList(data));
    }

    /**
     * @param code
     * @param responseCode
     * @param message
     */
    public NotFoundException(String code, String message) {
        super(code, HttpStatus.NOT_FOUND.value(), message);
    }

    
}
