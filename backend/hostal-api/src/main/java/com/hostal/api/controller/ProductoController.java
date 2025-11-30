package com.hostal.api.controller;

import com.hostal.api.dto.ProductoDto;
import com.hostal.api.entity.Producto;
import com.hostal.api.repository.ProductoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Gestión de productos del hostal")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public ResponseEntity<Page<Producto>> listarProductos(Pageable pageable) {
        Page<Producto> productos = productoRepository.findByActivo(true, pageable);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        return producto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Obtener productos por categoría")
    public ResponseEntity<List<Producto>> obtenerProductosPorCategoria(@PathVariable String categoria) {
        Producto.CategoriaProducto categoriaEnum = Producto.CategoriaProducto.valueOf(categoria.toUpperCase());
        List<Producto> productos = productoRepository.findByCategoriaAndActivo(categoriaEnum, true);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/precio")
    @Operation(summary = "Obtener productos por rango de precio")
    public ResponseEntity<List<Producto>> obtenerProductosPorPrecio(
            @RequestParam BigDecimal precioMin,
            @RequestParam BigDecimal precioMax) {
        List<Producto> productos = productoRepository.findByPrecioBetweenAndActivo(precioMin, precioMax, true);
        return ResponseEntity.ok(productos);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody ProductoDto.ProductoRequest productoRequest) {
        Producto producto = new Producto();
        producto.setNombre(productoRequest.getNombre());
        producto.setDescripcion(productoRequest.getDescripcion());
        producto.setPrecio(productoRequest.getPrecio());
        producto.setCategoria(Producto.CategoriaProducto.valueOf(productoRequest.getCategoria()));
        producto.setImagenUrl(productoRequest.getImagenUrl());
        producto.setStock(productoRequest.getStock());
        producto.setActivo(true);

        Producto productoGuardado = productoRepository.save(producto);
        return ResponseEntity.ok(productoGuardado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoDto.ProductoRequest productoRequest) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setNombre(productoRequest.getNombre());
            producto.setDescripcion(productoRequest.getDescripcion());
            producto.setPrecio(productoRequest.getPrecio());
            producto.setCategoria(Producto.CategoriaProducto.valueOf(productoRequest.getCategoria()));
            producto.setImagenUrl(productoRequest.getImagenUrl());
            producto.setStock(productoRequest.getStock());
            
            Producto productoActualizado = productoRepository.save(producto);
            return ResponseEntity.ok(productoActualizado);
        }
        
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/disponibilidad")
    @Operation(summary = "Cambiar disponibilidad del producto")
    public ResponseEntity<Producto> cambiarDisponibilidad(@PathVariable Long id, @RequestParam boolean disponible) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setActivo(disponible);
            
            Producto productoActualizado = productoRepository.save(producto);
            return ResponseEntity.ok(productoActualizado);
        }
        
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (soft delete)")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setActivo(false);
            productoRepository.save(producto);
            return ResponseEntity.ok().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}