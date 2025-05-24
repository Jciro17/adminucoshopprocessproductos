package com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.publicity;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.publicity.PublicityDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PublicityRepository extends JpaRepository<PublicityDomain, UUID> {
}
