package com.luciano.productoapi.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<String> mensajes;
    private String ruta;

    public ApiError() {}

    public ApiError(LocalDateTime timestamp, int status, String error, List<String> mensajes, String ruta) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.mensajes = mensajes;
        this.ruta = ruta;
    }

    // Getters y setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<String> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<String> mensajes) {
        this.mensajes = mensajes;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
