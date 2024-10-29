package com.cesarmaydana.cursojava.dto;

public class SaleProductDto {
    private String nombre;
    private int cantidad;
    private Double precio;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio(){
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
