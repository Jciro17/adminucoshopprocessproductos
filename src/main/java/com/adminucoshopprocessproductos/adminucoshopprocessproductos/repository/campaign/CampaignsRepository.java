package com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.campaign;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.campaign.Campaigns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CampaignsRepository extends JpaRepository<Campaigns, UUID> {
}
