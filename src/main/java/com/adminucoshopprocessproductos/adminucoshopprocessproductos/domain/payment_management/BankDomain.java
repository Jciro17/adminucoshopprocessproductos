package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.payment_management;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "bank")
public class BankDomain {

    public BankDomain() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_bank", nullable = false)
    private UUID idBank;

    @Column(name = "name", nullable = false)
    private String name;

    public BankDomain(UUID idBank, String name) {
        this.idBank = idBank;
        this.name = name;
    }
}
