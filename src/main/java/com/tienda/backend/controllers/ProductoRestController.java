package com.tienda.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.backend.models.entity.Producto;
import com.tienda.backend.models.service.ProductoService;

@CrossOrigin(origins="*", allowedHeaders = "*")
@RestController
@RequestMapping(value="/api")
public class ProductoRestController {
	
	@Autowired
	private ProductoService productoService;
	
	@GetMapping(value="/productos", produces = "application/json")
	public ResponseEntity<?> showAll(){
		List<Producto> productos = null;
		Map<String, Object> response = new HashMap<>();
		try {
			productos = productoService.findAll();
		}
		catch(DataAccessException ex) {
			response.put("mensaje", "Error al realizar la consulta");
			response.put("error", ex.getMessage() + ": " + ex.getMostSpecificCause().getMessage());
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(productos.size() == 0) {
			response.put("mensaje", "No hay productos registrados en la base de datos.");
			response.put("productos",productos);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
		}
		
		response.put("mensaje", "Actualmente la base de datos cuenta con " + productos.size() + " registros.");
		response.put("productos",productos);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
	}
		
}



