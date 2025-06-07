package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.payment_management.BankDomain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "name")
    private String name;

    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "type_of_document_id")
    private TypeOfDocumentDomain typeOfDocument;

    @Column(name = "document")
    private String document;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private RolDomain rol;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private BankDomain bank;

    @ManyToOne
    @JoinColumn(name = "account_type_id")
    private AccountTypeDomain accountType;

    @Column(name = "account")
    private String account;

    @Column(name = "password")
    private String password;
}
