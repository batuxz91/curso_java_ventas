package com.cesarmaydana.cursojava.service;

import com.cesarmaydana.cursojava.model.Category;
import com.cesarmaydana.cursojava.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> obtenerCategorias() {

        return categoryRepository.findAll();
    }

    public Optional<Category> obtenerCategoriaPorId(Long id) {

        return categoryRepository.findById(id);
    }

    public Category crearCategoria(Category category) {

        return categoryRepository.save(category);
    }

    public Optional<Category> actualizarCategoria(Long id, Category categoryActualizado) {
        return categoryRepository.findById(id).map(category -> {
            category.setNombre(categoryActualizado.getNombre());
            return categoryRepository.save(category);
        });
    }
}
