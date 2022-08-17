package com.tienda.backend.exceptions;

import java.util.ArrayList;
import java.util.List;

import com.tienda.backend.dtos.ErrorDto;

public abstract class TiendaException extends Exception {
    private final String code;
    private final int responseCode;
    private final List<ErrorDto> errorList = new ArrayList<>();

    /**
     * @param code
     * @param responseCode
     * @param message
     */
    public TiendaException(String code, int responseCode, String message) {
        super(message);
        this.code = code;
        this.responseCode = responseCode;
    }

    /**
     * @param code
     * @param responseCode
     * @param message
     */
    public TiendaException(String code, int responseCode, String message, List<ErrorDto> errorList) {
        super(message);
        this.code = code;
        this.responseCode = responseCode;
        this.errorList.addAll(errorList);
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @return the responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @return the errorList
     */
    public List<ErrorDto> getErrorList() {
        return errorList;
    }
}
