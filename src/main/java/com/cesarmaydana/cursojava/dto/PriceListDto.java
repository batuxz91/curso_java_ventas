package com.cesarmaydana.cursojava.dto;

import java.time.LocalDateTime;

public class PriceListDto {
    private Double precio;
    private LocalDateTime fechaFinVigencia;

    public PriceListDto(Double precio, LocalDateTime fechaVigencia) {
        this.precio = precio;
        this.fechaFinVigencia = fechaVigencia;
    }

    // Getters y Setters
    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public LocalDateTime getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    public void setFechaFinVigencia(LocalDateTime fechaFinVigencia) {
        this.fechaFinVigencia = fechaFinVigencia;
    }
}
