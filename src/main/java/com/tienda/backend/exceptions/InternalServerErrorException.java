package com.tienda.backend.exceptions;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import com.tienda.backend.dtos.ErrorDto;

public class InternalServerErrorException extends TiendaException {
    private static final long serialVersionUID = 1L;

    /**
     * @param code
     * @param responseCode
     * @param message
     * @param errorList
     */
    public InternalServerErrorException(String code, String message, ErrorDto data) {
        super(code, HttpStatus.INTERNAL_SERVER_ERROR.value(), message, Arrays.asList(data));
    }

    /**
     * @param code
     * @param responseCode
     * @param message
     */
    public InternalServerErrorException(String code, String message) {
        super(code, HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
    
}
