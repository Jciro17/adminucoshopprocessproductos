package com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.category;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.category.CategoryDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryDomain, UUID> {

    boolean existsByNameIgnoreCase(String name);

}
