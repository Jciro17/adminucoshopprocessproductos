package com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.product_management;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.product_management.ProductDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductDomain, UUID> {
    boolean existsByName(String name);
}

