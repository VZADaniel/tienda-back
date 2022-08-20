package com.tienda.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tienda.backend.exceptions.InternalServerErrorException;
import com.tienda.backend.exceptions.NotFoundException;
import com.tienda.backend.exceptions.TiendaException;
import com.tienda.backend.models.entity.Producto;
import com.tienda.backend.repositories.ProductoRepository;

@Service
public class ProductoServiceImpl implements iProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private iUploadFileService uploadFileService;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() throws TiendaException {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAllByStockGreaterThan(int stock) {
        return productoRepository.findAllByStockGreaterThanEqual(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAllOrderByStockDesc() throws TiendaException {
        return productoRepository.findAllByOrderByStockDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findByCodigo(String codigo) throws TiendaException {
        return productoRepository.findByCodigo(codigo).orElseThrow(
                () -> new NotFoundException("PRODUCTO_NOT_FOUND_404", "PRODUCTO CÓDIGO: " + codigo + " NO ENCONTRADO"));
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findById(Long id) throws TiendaException {
        return productoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("PRODUCTO_NOT_FOUND_404", "PRODUCTO ID: " + id + " NO ENCONTRADO"));
    }

    @Override
    @Transactional
    public Producto save(Producto producto) throws TiendaException {
        Producto nuevoProducto = null;
        if (producto.getId() != null) {
            productoRepository.findById(producto.getId())
                    .orElseThrow(() -> new NotFoundException("PRODUCTO_NOT_FOUND_404",
                            "PRODUCTO ID: " + producto.getId() + " NO ENCONTRADO"));
        }

        try {
            nuevoProducto = productoRepository.save(producto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR", "Error al ejecutar la consulta");
        }

        return nuevoProducto;
    }

    @Override
    @Transactional
    public String deleteByCodigo(String codigo) throws TiendaException {
        Producto producto = productoRepository.findByCodigo(codigo).orElseThrow(
                () -> new NotFoundException("PRODUCTO_NOT_FOUND_404", "PRODUCTO CÓDIGO: " + codigo + "NO ENCONTRADO"));

        try {
            uploadFileService.delete(producto.getFoto());
            productoRepository.deleteByCodigo(codigo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR", "Error al ejecutar la consulta");
        }

        return "PRODUCTO CÓDIGO: " + codigo + " ELIMINADO CON ÉXITO";
    }

    @Override
    @Transactional
    public String deleteById(Long id) throws TiendaException {
        Producto producto = productoRepository.findById(id).orElseThrow(
                () -> new NotFoundException("PRODUCTO_NOT_FOUND_404", "PRODUCTO ID: " + id + "NO ENCONTRADO"));

        try {
            if(id > 16){
                uploadFileService.delete(producto.getFoto());
            }
            productoRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("INTERNAL_SERVER_ERROR", "Error al ejecutar la consulta");
        }

        return "PRODUCTO ID: " + id + " ELIMINADO CON ÉXITO";
    }
}
