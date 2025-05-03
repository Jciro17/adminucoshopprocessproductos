package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.category;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "category")
public class CategoryDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "categoryId", updatable = false, nullable = false)
    private UUID categoryId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 255)
    private String description;


    public UUID getCategoryId() {
        return categoryId;
    }

    public void setIdCategory(UUID idCategory) {
        this.categoryId = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
