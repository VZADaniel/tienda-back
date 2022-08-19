package com.tienda.backend.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.DocumentException;
import com.tienda.backend.exceptions.NotFoundException;
import com.tienda.backend.models.dtos.ProductoDto;
import com.tienda.backend.models.entity.Producto;
import com.tienda.backend.responses.ProductoResponse;
import com.tienda.backend.services.iProductoService;
import com.tienda.backend.services.iUploadFileService;
import com.tienda.backend.view.pdf.ProductoPdf;

@RestController
@CrossOrigin()
@RequestMapping("/api/productos")
public class ProductoRestController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private iProductoService productoService;

    @Autowired
    private iUploadFileService uploadFileService;

    private ModelMapper mapper = new ModelMapper();

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductoResponse<List<Producto>> findAll() {
        List<Producto> productos = null;
        try {
            productos = productoService.findAll();
        } catch (Exception e) {
            return new ProductoResponse<>("INTERNAL_SERVER_ERROR", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                    "No se ha podido ejecutar al consulta");
        }

        return new ProductoResponse<>("OK", String.valueOf(HttpStatus.OK),
                "Productos encontrados ".concat(String.valueOf(productos.size())),
                productos);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/by-stock-desc", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductoResponse<List<Producto>> findAllOrderByStockDesc() {
        List<Producto> productos = null;
        try {
            productos = productoService.findAllOrderByStockDesc();
        } catch (Exception e) {
            return new ProductoResponse<>("INTERNAL_SERVER_ERROR", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                    "No se ha podido ejecutar al consulta");
        }

        return new ProductoResponse<>("OK", String.valueOf(HttpStatus.OK),
                "Productos encontrados ".concat(String.valueOf(productos.size())),
                productos);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/stock-gt-zero", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductoResponse<List<Producto>> findAllByStockGreaterThan() {
        List<Producto> productos = null;
        try {
            productos = productoService.findAllByStockGreaterThan(1);
        } catch (Exception e) {
            return new ProductoResponse<>("INTERNAL_SERVER_ERROR", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                    "No se ha podido ejecutar al consulta");
        }

        return new ProductoResponse<>("OK", String.valueOf(HttpStatus.OK),
                "Productos encontrados ".concat(String.valueOf(productos.size())),
                productos);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductoResponse<Producto> findById(@PathVariable long id) {
        Producto producto = null;
        try {
            producto = productoService.findById(id);
        } catch (NotFoundException notFound) {
            return new ProductoResponse<>(notFound.getCode(), String.valueOf(HttpStatus.NOT_FOUND),
                    notFound.getMessage());
        } catch (Exception e) {
            return new ProductoResponse<>("INTERNAL_SERVER_ERROR", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                    "No se ha podido ejecutar al consulta");
        }

        return new ProductoResponse<>("OK", String.valueOf(HttpStatus.OK),
                "Producto ID:" + id + " encontrado",
                producto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductoResponse<Producto> findByCodigo(@PathVariable String codigo) {
        Producto producto = null;
        try {
            producto = productoService.findByCodigo(codigo);
        } catch (NotFoundException notFound) {
            return new ProductoResponse<>(notFound.getCode(), String.valueOf(HttpStatus.NOT_FOUND),
                    notFound.getMessage());
        } catch (Exception e) {
            return new ProductoResponse<>("INTERNAL_SERVER_ERROR", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                    "No se ha podido ejecutar al consulta");
        }

        return new ProductoResponse<>("OK", String.valueOf(HttpStatus.OK),
                "Producto CÓDIGO:" + codigo + " encontrado",
                producto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductoResponse<Producto> save(@ModelAttribute ProductoDto producto) {
        Producto nuevoProducto = null;
        Producto productoEntity = convertirAEntity(producto);

        try {
            String uriFoto = uploadFileService.copy(producto.getFoto());
            productoEntity.setFoto(uriFoto);
            nuevoProducto = productoService.save(productoEntity);
        } catch (Exception e) {
            return new ProductoResponse<>("INTERNAL_SERVER_ERROR",
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                    "No se ha podido ejecutar al consulta");
        }

        return new ProductoResponse<>("CREATED", String.valueOf(HttpStatus.CREATED),
                "Producto creado con éxito",
                nuevoProducto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductoResponse<Producto> update(@ModelAttribute ProductoDto producto, @PathVariable long id) {
        Producto productoEntity = convertirAEntity(producto);
        Producto productoActualizado = null;

        productoEntity.setId(id);

        try {
            Producto productoDb = productoService.findById(id);

            if (productoDb.getId() != null && productoDb.getFoto() != null && productoDb.getFoto().length() > 0) {
                uploadFileService.delete(productoDb.getFoto());
            }

            String uriFoto = uploadFileService.copy(producto.getFoto());
            productoEntity.setFoto(uriFoto);
            productoActualizado = productoService.save(productoEntity);
        } catch (NotFoundException notFound) {
            return new ProductoResponse<>(notFound.getCode(), String.valueOf(HttpStatus.NOT_FOUND),
                    notFound.getMessage());
        } catch (Exception e) {
            return new ProductoResponse<>("INTERNAL_SERVER_ERROR", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                    "No se ha podido ejecutar al consulta");
        }

        return new ProductoResponse<>("UPDATED", String.valueOf(HttpStatus.OK),
                "Producto ID:" + id + " actualizado con éxito", productoActualizado);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductoResponse<String> deleteById(@PathVariable long id) {
        String message = "";
        try {
            message = productoService.deleteById(id);
        } catch (NotFoundException notFound) {
            return new ProductoResponse<>(notFound.getCode(), String.valueOf(HttpStatus.NOT_FOUND),
                    notFound.getMessage());
        } catch (Exception e) {
            return new ProductoResponse<>("INTERNAL_SERVER_ERROR", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                    "No se ha podido ejecutar al consulta");
        }

        return new ProductoResponse<>("DELETED", String.valueOf(HttpStatus.OK), message);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/codigo/{codigo}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductoResponse<String> deleteByCodigo(@PathVariable String codigo) {
        String message = "";
        try {
            message = productoService.deleteByCodigo(codigo);
        } catch (NotFoundException notFound) {
            return new ProductoResponse<>(notFound.getCode(), String.valueOf(HttpStatus.NOT_FOUND),
                    notFound.getMessage());
        } catch (Exception e) {
            return new ProductoResponse<>("INTERNAL_SERVER_ERROR", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR),
                    "No se ha podido ejecutar al consulta");
        }

        return new ProductoResponse<>("DELETED", String.valueOf(HttpStatus.OK), message);
    }

    /**
     * Endpoint para cargar las imagenes que se son agregadas mediante los
     * formularios
     * 
     * @param filename
     * @return ResponseEntity<Resource>
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/uploads/temp/{filename:.+}")
    public ResponseEntity<Resource> getNewPhoto(@PathVariable String filename) {
        Resource recurso = null;
        try {
            recurso = uploadFileService.load("temp/".concat(filename));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + recurso.getFilename())
                .body(recurso);
    }

    /**
     * Endpoint para cargar las imagenes que son precargadas al inciar el proyecto
     * 
     * @param filename
     * @return ResponseEntity<Resource>
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/uploads/{filename:.+}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        Resource recurso = null;
        try {
            recurso = uploadFileService.load(filename);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"" + recurso.getFilename())
                .body(recurso);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/export", produces = MediaType.APPLICATION_PDF_VALUE)
    public void exportProductsToPdf(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=productos_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        List<Producto> productos = null;
        try {
            productos = productoService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProductoPdf exporter = new ProductoPdf(productos);
        exporter.export(response);
    }

    /**
     * 
     * @param productoDto
     * @return Producto
     */
    public Producto convertirAEntity(ProductoDto productoDto) {
        if (productoDto != null) {
            return mapper.map(productoDto, Producto.class);
        } else {
            return null;
        }
    }

    /**
     * @param producto
     * @return ProductoDto productoDto
     */
    public ProductoDto convertirADto(Producto producto) {
        if (producto != null) {
            return mapper.map(producto, ProductoDto.class);
        } else {
            return null;
        }
    }
}
