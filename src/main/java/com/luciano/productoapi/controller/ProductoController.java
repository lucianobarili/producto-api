package com.luciano.productoapi.controller;

import com.luciano.productoapi.dto.ProductoRequestDTO;
import com.luciano.productoapi.dto.ProductoResponseDTO;
import com.luciano.productoapi.model.Producto;
import com.luciano.productoapi.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@RequestBody @Valid ProductoRequestDTO dto) {
        ProductoResponseDTO creado = service.crearProducto(dto);
        return ResponseEntity.status(201).body(creado);
    }



    @GetMapping
    public ResponseEntity<Page<ProductoResponseDTO>> listarProductos(Pageable pageable) {
        Page<ProductoResponseDTO> pagina = service.obtenerProductos(pageable);
        return ResponseEntity.ok(pagina);
    }


}
