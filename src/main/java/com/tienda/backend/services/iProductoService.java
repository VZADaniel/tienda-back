package com.tienda.backend.services;

import java.util.List;

import com.tienda.backend.exceptions.TiendaException;
import com.tienda.backend.models.entity.Producto;

public interface iProductoService {
    public List<Producto> findAll() throws TiendaException;

    public List<Producto> findAllOrderByStockDesc() throws TiendaException;

    public Producto findById(Long id) throws TiendaException;

    public Producto findByCodigo(String codigo) throws TiendaException;

    public Producto save(Producto producto) throws TiendaException;

    public String deleteById(Long id) throws TiendaException;

    public String deleteByCodigo(String codigo) throws TiendaException;
}
