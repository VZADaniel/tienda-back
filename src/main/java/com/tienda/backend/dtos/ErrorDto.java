package com.tienda.backend.dtos;

import java.io.Serializable;

public class ErrorDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;

    /**
     * @param name
     * @param value
     */
    public ErrorDto(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
