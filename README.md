# 🛒 Producto API

API REST para la gestión de productos desarrollada en Java 17 con Spring Boot.  
Incluye CRUD completo, validaciones, paginación, manejo de errores y pruebas unitarias con JUnit 5 y Mockito.

---

## 🚀 Tecnologías utilizadas

- Java 17
- Spring Boot 3
- Maven
- Spring Data JPA
- H2 Database (modo en memoria)
- Spring Validation
- JUnit 5 + Mockito
- Jacoco (análisis de cobertura)
- Postman (para pruebas manuales)

---

## ✅ Funcionalidades

- Crear productos con validaciones (`@Valid`)
- Obtener lista de productos con paginación y ordenamiento
- Respuestas claras en errores de validación (handler global)
- Tests unitarios del Service y Controller
- Mocks de dependencias con Mockito
- Generación de cobertura de código con Jacoco

---

## 📦 Endpoints principales

### Crear producto
POST /productos

Body (JSON):
```json
{
  "nombre": "Teclado",
  "descripcion": "Teclado mecánico",
  "precio": 3500,
  "stock": 10
}
```


Listar productos


GET /productos?page=0&size=10&sort=precio,asc


🧪 Cómo correr los tests

mvn clean test

Y para ver el reporte de cobertura de Jacoco:

open target/site/jacoco/index.html


👨‍💻 Autor
Luciano Barili
Desarrollador Backend Jr | Especialista en Derecho Empresario

