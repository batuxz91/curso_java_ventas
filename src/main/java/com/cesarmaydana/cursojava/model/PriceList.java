package com.cesarmaydana.cursojava.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "listaprecios")
public class PriceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Product producto;
    private Double precio;
    private LocalDateTime fechaFinVigencia;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

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
