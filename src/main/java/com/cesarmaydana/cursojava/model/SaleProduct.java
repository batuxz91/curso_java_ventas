package com.cesarmaydana.cursojava.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ventaproducto")

public class SaleProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product producto;

    @ManyToOne
    private Sale venta;

    @ManyToOne
    @JoinColumn(name = "price_list_id", nullable = false)
    private PriceList listaPrecioVigente;

    private int cantidad;

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProducto() {
        return producto;
    }

    public void setProducto(Product producto) {
        this.producto = producto;
    }

    public Sale getVenta() {
        return venta;
    }

    public void setVenta(Sale venta) {
        this.venta = venta;
    }

    public PriceList getListaPrecioVigente() {
        return listaPrecioVigente;
    }

    public void setListaPrecioVigente(PriceList listaPrecioVigente) {
        this.listaPrecioVigente = listaPrecioVigente;
    }


}
