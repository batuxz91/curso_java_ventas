package com.cesarmaydana.cursojava.dto;

import com.cesarmaydana.cursojava.model.Client;

import java.time.LocalDate;
import java.util.List;

public class SaleDto {
    private Long id;
    private LocalDate fecha;
    private Client cliente;
    private Double monto;
    private List<SaleProductDto> productos;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Client getCliente() {
        return cliente;
    }

    public void setClienteId(Client cliente) {
        this.cliente = cliente;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public List<SaleProductDto> getProductos() {
        return productos;
    }

    public void setProductos(List<SaleProductDto> productos) {
        this.productos = productos;
    }
}
