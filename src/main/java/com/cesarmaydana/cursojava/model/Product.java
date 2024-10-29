package com.cesarmaydana.cursojava.model;

import com.cesarmaydana.cursojava.clases.Persistence;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "productos")
public class Product extends Persistence {

    private String nombre;
    private Integer stock; // Mantener el campo stock

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PriceList> listaPrecios;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Category categoria;

    @ManyToOne
    @JoinColumn(name = "marca_id")
    private SaleMark marca;

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

    public List<PriceList> getListaPrecios() {
        return listaPrecios;
    }

    public void setListaPrecios(List<PriceList> listaPrecios) {
        this.listaPrecios = listaPrecios;
    }
}
