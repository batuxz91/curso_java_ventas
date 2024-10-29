package com.cesarmaydana.cursojava.dto;

import java.util.List;

public class PriceListResponseDto {

    private Long id;
    private String nombre;
    private int stock;
    private List<PriceListDto> precios;

    public PriceListResponseDto(Long id, String nombre, int stock, List<PriceListDto> listaPreciosHistoricos) {
        this.id = id;
        this.nombre = nombre;
        this.stock = stock;
        this.precios = listaPreciosHistoricos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PriceListDto> getPrecios() {
        return precios;
    }

    public void setPrecios(List<PriceListDto> precios) {
        this.precios = precios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }




}
