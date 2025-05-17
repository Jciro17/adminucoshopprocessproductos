package com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.product_management;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.product_management.ProductDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.category.CategoryRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.product_management.ProductRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.notification.NotificationService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
@NoArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
    private ObjectMapper objectMapper;
    private NotificationService notificationService;
    private CategoryRepository categoryRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, ObjectMapper objectMapper, NotificationService notificationService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDomain> findAll() {

        return productRepository.findAll();

    }

    public ProductDomain findById(UUID productId) {

        return validateProductDoesExist(productId);

    }

    public void saveProduct(ProductDomain product) {

        if (product.getProductId() != null && productRepository.existsById(product.getProductId())) {
            throw new RuntimeException("El Id del producto ya se encuentra en uso.");
        }

        validateProductName(product.getName());

        if (product.getCategory() != null && product.getCategory().getCategoryId() != null) {
            validateCategoryExists(product.getCategory().getCategoryId());
        }

        productRepository.save(product);
    }

    public void deleteProduct(UUID productId) {
        if (productId == null) {
            throw new RuntimeException("El ID del producto a eliminar no puede ser nulo.");
        }
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("El producto que desea eliminar no existe.");
        }

        productRepository.deleteById(productId);

    }

    public void updateProduct(UUID productId, ProductDomain newProduct) {


        if (newProduct == null || isProductEmpty(newProduct)) {
            throw new RuntimeException("No se puede actualizar el producto ya que la información se encuentra nula o vacía.");
        }

        validateProductName(newProduct.getName());

        if (newProduct.getCategory() != null && newProduct.getCategory().getCategoryId() != null) {
            validateCategoryExists(newProduct.getCategory().getCategoryId());
        }

        ProductDomain existingProduct = validateProductDoesExist(productId);

        validateDifferentId(existingProduct, newProduct, productId);

        productRepository.save(newProduct);
    }

    public void patchProduct(UUID productId, Map<String, Object> updates) throws JsonMappingException {

        if (updates == null || updates.isEmpty()) {
            throw new RuntimeException("No se puede actualizar el producto ya que la información se encuentra nula o vacía.");
        }

        validatePatchProduct(updates);

        ProductDomain existingProduct = validateProductDoesExist(productId);

        objectMapper.updateValue(existingProduct, updates);

        productRepository.save(existingProduct);

    }

    private boolean isProductEmpty(ProductDomain product) {

        return (product.getName() == null || product.getName().trim().isEmpty()) && (product.getDescription() == null || product.getDescription().trim().isEmpty()) && (product.getPrice() <= 0.0) && (product.getStock() <= 0);
    }

    private void validatePatchProduct(Map<String, Object> product) {

        validateProductPatchName(product);

        validateProductPatchCategory(product);
    }

    private void validateProductPatchName(Map<String, Object> product) {

        if (!product.containsKey("name")) return;

        if (product.get("name") == null) {
            throw new RuntimeException("El nombre del producto no puede ser nulo.");
        }

        String name = product.get("name").toString();

        validateProductName(name);

    }

    private void validateProductPatchCategory(Map<String, Object> product) {

        if (!product.containsKey("category")) return;

        Object categoryObj = product.get("category");
        if (!(categoryObj instanceof Map<?, ?> categoryMap)) {
            throw new RuntimeException("El campo 'category' tiene un formato inválido");
        }

        if (!categoryMap.containsKey("categoryId")) {
            throw new RuntimeException("El objeto categoría no contiene el campo 'categoryId'");
        }

        Object categoryIdObj = categoryMap.get("categoryId");

        UUID categoryId = parseCategoryPatchId(categoryIdObj);

        if (categoryId != null) {
            validateCategoryExists(categoryId);
        }

    }

    private UUID parseCategoryPatchId(Object categoryIdObj) {

        try {
            return UUID.fromString(categoryIdObj.toString());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("El ID de la categoría no tiene un formato UUID válido.");
        }

    }

    private void validateProductName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("El nombre del producto no puede ser nulo.");
        }
        if (productRepository.existsByName(name.trim())) {
            throw new RuntimeException("Ya existe un producto con el mismo nombre.");
        }

    }

    private void validateCategoryExists(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("La categoría ingresada no existe.");
        }
    }

    private ProductDomain validateProductDoesExist(UUID id) {

        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("El producto no existe."));

    }

    private void validateDifferentId(ProductDomain newProduct, ProductDomain existingProduct, UUID id) {
        if (!id.equals(newProduct.getProductId()) && !existingProduct.getProductId().equals(newProduct.getProductId())) {
            throw new RuntimeException("No se puede modificar el Id del producto.");
        }
    }

}