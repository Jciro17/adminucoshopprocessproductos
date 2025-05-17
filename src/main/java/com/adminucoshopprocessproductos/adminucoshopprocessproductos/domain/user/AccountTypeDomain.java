package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "accountType")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AccountTypeDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountTypeId;

    @Column
    private String accountTypeName;

    @OneToMany(mappedBy = "accountType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserDomain> users;
}
