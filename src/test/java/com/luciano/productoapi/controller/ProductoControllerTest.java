package com.luciano.productoapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luciano.productoapi.dto.ProductoRequestDTO;
import com.luciano.productoapi.dto.ProductoResponseDTO;
import com.luciano.productoapi.service.ProductoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // ✅ FALTABA
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearProducto_deberiaResponderCon201YBodyEsperado() throws Exception {
        // Arrange
        ProductoRequestDTO requestDTO = new ProductoRequestDTO(
                "Mouse",
                "Mouse inalámbrico",
                new BigDecimal("2500"),
                15
        );

        ProductoResponseDTO responseDTO = new ProductoResponseDTO(
                1L,
                "Mouse",
                "Mouse inalámbrico",
                new BigDecimal("2500"),
                15,
                LocalDateTime.now()
        );

        when(service.crearProducto(any(ProductoRequestDTO.class))).thenReturn(responseDTO);

        // Act + Assert
        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Mouse"))
                .andExpect(jsonPath("$.precio").value(2500))
                .andExpect(jsonPath("$.stock").value(15));

        verify(service, times(1)).crearProducto(any(ProductoRequestDTO.class));
    }


    @Test
    void listarProductos_deberiaResponderCon200YListaPaginada() throws Exception {
        // Arrange
        ProductoResponseDTO producto1 = new ProductoResponseDTO(
                1L,
                "Producto A",
                "Desc A",
                new BigDecimal("100"),
                10,
                LocalDateTime.now()
        );

        ProductoResponseDTO producto2 = new ProductoResponseDTO(
                2L,
                "Producto B",
                "Desc B",
                new BigDecimal("200"),
                20,
                LocalDateTime.now()
        );

        Page<ProductoResponseDTO> pagina = new PageImpl<>(List.of(producto1, producto2));

        when(service.obtenerProductos(any(Pageable.class))).thenReturn(pagina);

        // Act + Assert
        mockMvc.perform(get("/productos")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "precio,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].nombre").value("Producto A"))
                .andExpect(jsonPath("$.content[1].precio").value(200));
    }

    @Test
    void crearProducto_conNombreVacio_deberiaResponderCon400() throws Exception {
        ProductoRequestDTO requestDTO = new ProductoRequestDTO(
                "", // nombre vacío → @NotBlank debería fallar
                "Mouse inalámbrico",
                new BigDecimal("2500"),
                15
        );

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validación fallida"))
                .andExpect(jsonPath("$.mensajes").isArray());
    }


    @Test
    void crearProducto_conFallaInterna_deberiaResponderCon500() throws Exception {
        ProductoRequestDTO requestDTO = new ProductoRequestDTO(
                "Mouse",
                "Mouse inalámbrico",
                new BigDecimal("2500"),
                15
        );

        when(service.crearProducto(any(ProductoRequestDTO.class)))
                .thenThrow(new RuntimeException("Error inesperado"));

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Error interno del servidor"))
                .andExpect(jsonPath("$.status").value(500));
    }



    @Test
    void crearProducto_conViolacionDeRestriccion_deberiaResponderCon400() throws Exception {
        ProductoRequestDTO requestDTO = new ProductoRequestDTO(
                "Mouse",
                "Mouse inalámbrico",
                new BigDecimal("2500"),
                15
        );

        ConstraintViolation<?> mockViolation = mock(ConstraintViolation.class);
        when(mockViolation.getMessage()).thenReturn("Precio inválido");

        ConstraintViolationException exception = new ConstraintViolationException(
                Set.of(mockViolation)
        );

        when(service.crearProducto(any(ProductoRequestDTO.class))).thenThrow(exception);

        mockMvc.perform(post("/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Restricción de datos"))
                .andExpect(jsonPath("$.mensajes[0]").value("Precio inválido"));
    }


}
