package com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.campaign;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.TypeOfDocumentDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeDocumentRepository extends JpaRepository<TypeOfDocumentDomain, Integer> {
}
