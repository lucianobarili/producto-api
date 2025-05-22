package com.luciano.productoapi.service;

import com.luciano.productoapi.dto.ProductoRequestDTO;
import com.luciano.productoapi.dto.ProductoResponseDTO;
import com.luciano.productoapi.model.Producto;
import com.luciano.productoapi.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.util.function.Function;

@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        // Crear producto sin usar builder
        Producto producto = new Producto(
                null,
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getStock(),
                null // fechaPublicacion se setea automáticamente en @PrePersist
        );

        Producto guardado = repository.save(producto);

        return new ProductoResponseDTO(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getDescripcion(),
                guardado.getPrecio(),
                guardado.getStock(),
                guardado.getFechaPublicacion()
        );
    }

    public Page<ProductoResponseDTO> obtenerProductos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(new Function<Producto, ProductoResponseDTO>() {
                    @Override
                    public ProductoResponseDTO apply(Producto producto) {
                        return new ProductoResponseDTO(
                                producto.getId(),
                                producto.getNombre(),
                                producto.getDescripcion(),
                                producto.getPrecio(),
                                producto.getStock(),
                                producto.getFechaPublicacion()
                        );
                    }
                });
    }
}
