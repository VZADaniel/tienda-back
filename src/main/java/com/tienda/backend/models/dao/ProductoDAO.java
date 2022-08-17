package com.tienda.backend.models.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tienda.backend.models.entity.Producto;


@Repository
public interface ProductoDAO extends CrudRepository<Producto, Long>{

	
	
	
}
