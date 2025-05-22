package com.luciano.productoapi.config;

import com.luciano.productoapi.model.Producto;
import com.luciano.productoapi.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class TestDataInitializer {

    @Bean
    public CommandLineRunner initData(ProductoRepository repository) {
        return args -> {
            Producto producto = new Producto(
                    null,
                    "Producto de prueba",
                    "Este producto fue creado al iniciar la app",
                    new BigDecimal("199.99"),
                    10,
                    null // fechaPublicacion se asigna con @PrePersist
            );

            repository.save(producto);
        };
    }
}
