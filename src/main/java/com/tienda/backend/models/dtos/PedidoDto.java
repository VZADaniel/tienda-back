package com.tienda.backend.models.dtos;

import java.io.Serializable;
import java.util.List;

public class PedidoDto implements Serializable {
    private final static long serialVersionUID = 1L;

    private ClienteDto cliente;
    private EnvioDto envio;
    private List<ItemCartDto> productos;

    /**
     * @return the cliente
     */
    public ClienteDto getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(ClienteDto cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the envio
     */
    public EnvioDto getEnvio() {
        return envio;
    }

    /**
     * @param envio the envio to set
     */
    public void setEnvio(EnvioDto envio) {
        this.envio = envio;
    }

    /**
     * @return the productos
     */
    public List<ItemCartDto> getProductos() {
        return productos;
    }

    /**
     * @param productos the productos to set
     */
    public void setProductos(List<ItemCartDto> productos) {
        this.productos = productos;
    }
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        return "PedidoDto [cliente=" + cliente + ", envio=" + envio + ", productos=" + productos + "]";
    }

}
