package com.cesarmaydana.cursojava.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @ManyToOne
    private Client cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<SaleProduct> ventaProductos = new ArrayList<>();;

    private Double monto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getCliente() {
        return cliente;
    }

    public void setCliente(Client cliente) {
        this.cliente = cliente;
    }

    public List<SaleProduct> getVentaProductos() {
        return ventaProductos;
    }

    public void setVentaProductos(List<SaleProduct> ventaProductos) {
        this.ventaProductos = ventaProductos;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
