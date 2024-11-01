package com.cesarmaydana.cursojava.dto;

public class ProductDto {

    private Long productId;
    private int cantidad;

    public ProductDto(Long productId, int cantidad) {
        this.productId = productId;
        this.cantidad = cantidad;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long product_id) {
        this.productId = product_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


}
