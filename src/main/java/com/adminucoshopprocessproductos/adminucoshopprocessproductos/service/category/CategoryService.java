package com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.category;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.category.CategoryDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.category.CategoryRepository;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.NoSuchElementException;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }

    public List<CategoryDomain> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void saveCategory(CategoryDomain categoriaDomain) {

        if (categoriaDomain.getName() == null || categoriaDomain.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }
        if (categoriaDomain.getDescription() == null || categoriaDomain.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la categoría es obligatoria.");
        }
        if (categoryRepository.existsByNameIgnoreCase(categoriaDomain.getName().trim())) {
            throw new IllegalStateException("La categoría ya existe.");
        }

        categoryRepository.save(categoriaDomain);
    }

    public CategoryDomain getCategoryById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("El id es obligatorio.");
        }

        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("La categoría buscada no existe"));
    }

    public void deleteCategory(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("El id es obligatorio.");
        }

        if (!categoryRepository.existsById(id)) {
            throw new NoSuchElementException("La categoría a eliminar no existe.");
        }

        categoryRepository.deleteById(id);
    }

    public void updateCategory(CategoryDomain categoryDomain) {
        if (categoryDomain.getCategoryId() == null) {
            throw new IllegalArgumentException("El idCategoria es obligatorio.");
        }

        if (categoryDomain.getName() == null || categoryDomain.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio.");
        }

        if (categoryDomain.getDescription() == null || categoryDomain.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la categoría es obligatoria.");
        }

        if (!categoryRepository.existsById(categoryDomain.getCategoryId())) {
            throw new NoSuchElementException("La categoría a actualizar no existe.");
        }
        categoryRepository.save(categoryDomain);
    }

    public void updateCategoryByParams(UUID id, Map<String, Object> data) throws JsonMappingException {
        if (!categoryRepository.existsById(id)) {
            throw new NoSuchElementException("La categoría no existe");
        }

        Optional<CategoryDomain> optionalCategory = categoryRepository.findById(id);

        if (optionalCategory.isEmpty()) {
            throw new IllegalStateException("No se pudo obtener la categoría desde el repositorio.");
        }

        CategoryDomain category = optionalCategory.get();
        objectMapper.updateValue(category, data);
        categoryRepository.save(category);
    }
}

