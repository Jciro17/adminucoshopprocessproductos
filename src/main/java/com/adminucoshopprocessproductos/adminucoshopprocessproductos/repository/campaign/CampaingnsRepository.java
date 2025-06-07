package com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.campaign;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.campaign.Campaingns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CampaingnsRepository extends JpaRepository<Campaingns, UUID> {
}
