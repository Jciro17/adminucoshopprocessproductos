package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.product_management;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.category.CategoryDomain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_Id", updatable = false, nullable = false)
    private UUID productId;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_Id")
    private CategoryDomain category;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

}

