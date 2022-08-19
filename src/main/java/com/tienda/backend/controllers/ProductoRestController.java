package com.tienda.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		
		@GetMapping(value= "/productos/{id}", produces = "application/json")
		public ResponseEntity<?> showOne(@PathVariable Long id){
			Producto producto = null;
			Map<String, Object> response  = new HashMap<>();
			try {
				producto = productoService.findById(id);
			}
			catch(DataAccessException ex) {
				response.put("mensaje", "Error al realizar la consulta");
				response.put("error", ex.getMessage() + ": " + ex.getMostSpecificCause().getMessage());
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			if(producto == null) {
				response.put("mensaje", "El producto ID: " + id + " no existe en nuestros registros");
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<Producto>(producto,HttpStatus.OK);
		}
		
		
		@PostMapping(value ="/productos", produces = "application/json")
		public ResponseEntity<?> create(@RequestBody Producto producto){
			Producto productoNuevo = null;
			Map<String, Object> response = new HashMap<>();
			try {
				List<Producto> productos = productoService.findAll();
				for(Producto e: productos) {
					if(e.getNombre().equalsIgnoreCase(producto.getNombre())) {
						response.put("mensaje", "Error al realizar el registro de un nuevo producto. El nombre del producto ya existe en nuestros registros");
						return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
					}
				}
				productoNuevo = productoService.save(producto);
			}
			catch(DataAccessException ex) {
				response.put("mensaje", "Error al realizar la consulta");
				response.put("error", ex.getMessage() + ": " + ex.getMostSpecificCause().getMessage());
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			response.put("mensaje", "El producto ha sido registrado con éxito");
			response.put("producto",productoNuevo);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);		
		}
		
		
		@PutMapping(value= "/productos/{id}", produces = "application/json")
		public ResponseEntity<?> updatePut(@RequestBody Producto producto, @PathVariable Long id){
			Producto productoActual = productoService.findById(id);
			Producto productoActualizado = null;
			Map<String, Object> response = new HashMap<>();
			
			if(productoActual == null) {
				response.put("Mensaje", "No se puede editar. El producto ID: " + id + " no se encuentra en nuestros registros");
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
			try {
				productoActual.setCodigo(producto.getCodigo());
				productoActual.setNombre(producto.getNombre());
				productoActual.setDescripcion(producto.getDescripcion());
				productoActual.setPrecio(producto.getPrecio());
				productoActual.setStock(producto.getStock());
				productoActual.setFoto(producto.getFoto());
				productoActualizado = productoService.save(productoActual);
			}
			catch(DataAccessException ex) {
				response.put("mensaje", "Error al realizar la consulta");
				response.put("error", ex.getMessage() + ": " + ex.getMostSpecificCause().getMessage());
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			response.put("mensaje", "Producto actualizado con éxito");
			response.put("producto", productoActualizado);
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.ACCEPTED);
		}
		
		
		@DeleteMapping(value= "/productos/{id}", produces = "application/json")
		public ResponseEntity<?> delete(@PathVariable Long id){
			Producto producto = productoService.findById(id);
			Map<String, Object> response = new HashMap<>();
			if(producto == null) {
				response.put("mensaje", "El producto seleccionado no existe en nuestros registros");
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
			}
			try {
				productoService.delete(id);
			}
			catch(DataAccessException ex) {
				response.put("mensaje", "Error al realizar la consulta");
				response.put("error", ex.getMessage() + ": " + ex.getMostSpecificCause().getMessage());
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			response.put("mensaje", "Producto eliminado correctamente!");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.ACCEPTED);
		}
		
}



