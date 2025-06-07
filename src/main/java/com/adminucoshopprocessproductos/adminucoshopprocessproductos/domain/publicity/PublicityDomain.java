package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.publicity;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.campaign.Campaigns;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.product_management.ProductDomain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "publicity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PublicityDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "publicityId", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductDomain product;

    @ManyToOne
    @JoinColumn(name = "campaignId")
    private Campaigns campaign;

    @Column(name = "role")
    private String role;

    @Column(name = "message")
    private String message;

    @Column(name ="frequencyAmount")
    private int frequencyAmount;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;
}
