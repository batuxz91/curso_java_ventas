package com.cesarmaydana.cursojava.model;

import com.cesarmaydana.cursojava.clases.Persistence;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Sale extends Persistence {

    private LocalDate fecha;

    @ManyToOne
    private Client cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<SaleProduct> ventaProductos;

    private Double monto;

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

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
}
