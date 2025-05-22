package com.luciano.productoapi.service;

import com.luciano.productoapi.dto.ProductoRequestDTO;
import com.luciano.productoapi.dto.ProductoResponseDTO;
import com.luciano.productoapi.model.Producto;
import com.luciano.productoapi.repository.ProductoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductoServiceTest {

    @Mock
    private ProductoRepository repository; // Simula el repo real

    @InjectMocks
    private ProductoService service; // Usamos el service real

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada test
    }

    /* Qué voy a testear? Si le paso un DTO válido, construye correctamente el Producto y devuelve el DTO esperado */

    @Test
    void crearProducto_conDatosValidos_deberiaDevolverDTOCorrecto() {
        // Arrange
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "Teclado",
                "Teclado mecánico RGB",
                new BigDecimal("3500"),
                10
        );

        Producto guardado = new Producto(
                1L,
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getStock(),
                null
        );

        when(repository.save(any(Producto.class))).thenReturn(guardado);

        // Act
        ProductoResponseDTO resultado = service.crearProducto(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Teclado", resultado.getNombre());
        assertEquals("Teclado mecánico RGB", resultado.getDescripcion());
        assertEquals(new BigDecimal("3500"), resultado.getPrecio());
        assertEquals(10, resultado.getStock());

        verify(repository, times(1)).save(any(Producto.class));
    }

    /* Qué voy a testear? Si le paso un DTO con nombre largo y precio alto, construye correctamente el Producto y devuelve el DTO esperado */

    @Test
    void crearProducto_conNombreLargoYPrecioAlto_deberiaGuardarCorrectamente() {
        // Arrange
        String nombreLargo = "Producto " + "X".repeat(90);
        ProductoRequestDTO dto = new ProductoRequestDTO(
                nombreLargo,
                "Producto edición limitada, alta gama, materiales premium",
                new BigDecimal("99999.99"),
                3
        );

        Producto guardado = new Producto(
                2L,
                dto.getNombre(),
                dto.getDescripcion(),
                dto.getPrecio(),
                dto.getStock(),
                null
        );

        when(repository.save(any(Producto.class))).thenReturn(guardado);

        // Act
        ProductoResponseDTO resultado = service.crearProducto(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(dto.getNombre(), resultado.getNombre());
        assertEquals(dto.getDescripcion(), resultado.getDescripcion());
        assertEquals(dto.getPrecio(), resultado.getPrecio());
        assertEquals(dto.getStock(), resultado.getStock());

        verify(repository, times(1)).save(any(Producto.class));
    }

    @Test
    void crearProducto_repositoryDevuelveNull_deberiaLanzarExcepcion() {
        ProductoRequestDTO dto = new ProductoRequestDTO(
                "Producto Test",
                "Falla",
                new BigDecimal("123.45"),
                5
        );

        when(repository.save(any(Producto.class))).thenReturn(null);

        assertThrows(NullPointerException.class, () -> service.crearProducto(dto));
    }

}
