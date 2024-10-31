package com.cesarmaydana.cursojava.dto;

import java.time.LocalDate;
import java.util.List;

public class SaleRequestDto {

    private Long clientId;
    private LocalDate fecha;
    private List<ProductDto> products;

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

}
