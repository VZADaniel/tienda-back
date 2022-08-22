package com.tienda.backend.responses;

import java.io.Serializable;

public class ProductoResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String status;
    private int code;
    private String message;
    private T data;

    /**
     * @param status
     * @param code
     * @param message
     * @param data
     */
    public ProductoResponse(String status, int code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * @param status
     * @param code
     * @param message
     */
    public ProductoResponse(String status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }

}
