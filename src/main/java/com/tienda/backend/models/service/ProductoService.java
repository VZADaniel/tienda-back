package com.tienda.backend.models.service;

import java.util.List;

import com.tienda.backend.models.entity.Producto;

public interface ProductoService {

	public List<Producto> findAll();
	
	public Producto save(Producto producto);
	
	public void delete(Long id);
	
	public Producto findById(Long id);
	
	
}
