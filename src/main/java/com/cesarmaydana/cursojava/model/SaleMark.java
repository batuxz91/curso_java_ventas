package com.cesarmaydana.cursojava.model;

import com.cesarmaydana.cursojava.clases.Persistence;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="marcas")
public class SaleMark extends Persistence {

    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

