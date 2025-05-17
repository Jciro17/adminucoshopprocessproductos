package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "rol")
public class RolDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private int rolId;

    @Column(name = "rol_name")
    private String rolName;
}
