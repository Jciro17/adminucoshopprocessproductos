package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "type_of_document")
public class TypeOfDocumentDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_of_document_id")
    private int typeOfDocumentId;

    @Column(name = "type")
    private String type;
}
