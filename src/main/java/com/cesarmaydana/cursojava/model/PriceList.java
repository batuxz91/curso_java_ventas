package com.cesarmaydana.cursojava.model;

import com.cesarmaydana.cursojava.clases.Persistence;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "listaprecios")
public class PriceList extends Persistence {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Product producto;
    private Double precio;
    private LocalDateTime fechaFinVigencia;

    public Product getProducto() {
        return producto;
    }

    public void setProducto(Product producto) {
        this.producto = producto;
    }

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
