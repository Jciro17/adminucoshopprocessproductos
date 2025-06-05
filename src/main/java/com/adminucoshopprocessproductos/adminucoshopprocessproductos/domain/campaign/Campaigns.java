package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.campaign;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.category.CategoryDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.payment_management.BankDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.product_management.ProductDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.RolDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.TypeOfDocumentDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.UserDomain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "campaigns")
public class Campaigns {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product", nullable = true)
    private ProductDomain product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDomain user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private CategoryDomain category;

    @Column(name = "zone")
    private String zone;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private RolDomain role;

    @ManyToOne
    @JoinColumn(name = "id_documentType")
    private TypeOfDocumentDomain documentType;

    @ManyToOne
    @JoinColumn(name = "id_bank")
    private BankDomain bank;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "duration")
    private String duration;

    public Campaigns() {
    }

    public Campaigns(UUID id, ProductDomain product, UserDomain user, CategoryDomain category, String zone, RolDomain role,
                      TypeOfDocumentDomain documentType, BankDomain bank, LocalDate startDate, LocalDate endDate,
                      LocalDate registrationDate, String duration) {
        this.id = id;
        this.product = product;
        this.user = user;
        this.category = category;
        this.zone = zone;
        this.role = role;
        this.documentType = documentType;
        this.bank = bank;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registrationDate = registrationDate;
        this.duration = duration;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ProductDomain getProduct() {
        return product;
    }

    public void setProduct(ProductDomain product) {
        this.product = product;
    }

    public UserDomain getUser() {
        return user;
    }

    public void setUser(UserDomain user) {
        this.user = user;
    }

    public CategoryDomain getCategory() {
        return category;
    }

    public void setCategory(CategoryDomain category) {
        this.category = category;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public RolDomain getRole() {
        return role;
    }

    public void setRole(RolDomain role) {
        this.role = role;
    }

    public TypeOfDocumentDomain getDocumentType() {
        return documentType;
    }

    public void setDocumentType(TypeOfDocumentDomain documentType) {
        this.documentType = documentType;
    }

    public BankDomain getBank() {
        return bank;
    }

    public void setBank(BankDomain bank) {
        this.bank = bank;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
