package com.tienda.backend.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.backend.models.dao.ProductoDAO;
import com.tienda.backend.models.entity.Producto;

public class ProductoServiceImpl implements ProductoService {

	
	@Autowired
	private ProductoDAO productoDAO;
	
	
	@Override
	@Transactional(readOnly= true)
	public List<Producto> findAll() {
		
		return (List<Producto>) productoDAO.findAll();
	}

	@Override
	@Transactional
	public Producto save(Producto producto) {
		
		return productoDAO.save(producto);
	}

	
	@Override
	@Transactional
	public void delete(Long id) {
		productoDAO.deleteById(id);
		
	}
	

	@Override
	@Transactional(readOnly = true)
	public Producto findById(Long id) {
		
		return productoDAO.findById(id).orElse(null);
	}

	
}

