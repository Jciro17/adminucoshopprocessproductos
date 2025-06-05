package com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.publicity;


import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.campaign.Campaigns;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.product_management.ProductDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.publicity.PublicityDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.campaign.CampaignsRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.product_management.ProductRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.publicity.PublicityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PublicityService {

    private final PublicityRepository publicityRepository;
    private final CampaignsRepository campaignRepository;
    private final ProductRepository productRepository;

    public PublicityService(
            PublicityRepository publicityRepository,
            CampaignsRepository campaignRepository,
            ProductRepository productRepository
    ) {
        this.publicityRepository = publicityRepository;
        this.campaignRepository = campaignRepository;
        this.productRepository = productRepository;
    }

    public List<PublicityDomain> getAllPublicities() {
        return publicityRepository.findAll();
    }

    public ResponseEntity<PublicityDomain> getPublicityById(UUID id) {
        return publicityRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public PublicityDomain createPublicity(PublicityDomain publicity) {
        if (publicity == null || publicity.getCampaign() == null || publicity.getProduct() == null) {
            throw new IllegalArgumentException("Los datos de la publicidad no pueden ser nulos");
        }

        UUID campaignId = publicity.getCampaign().getId();
        UUID productId = publicity.getProduct().getProductId();

        Optional<Campaigns> campaignOpt = campaignRepository.findById(campaignId);
        Optional<ProductDomain> productOpt = productRepository.findById(productId);

        if (campaignOpt.isEmpty()) {
            throw new NoSuchElementException("La campaña asociada no existe");
        }

        if (productOpt.isEmpty()) {
            throw new NoSuchElementException("El producto asociado no existe");
        }

        Campaigns campaign = campaignOpt.get();

        if (!publicity.getStartDate().isEqual(campaign.getStartDate()) ||
                !publicity.getEndDate().isEqual(campaign.getEndDate())) {
            throw new IllegalArgumentException("Las fechas de la publicidad deben coincidir exactamente con las fechas de la campaña");
        }

        publicity.setCampaign(campaign);
        publicity.setProduct(productOpt.get());

        return publicityRepository.save(publicity);
    }

    public PublicityDomain updatePublicity(UUID id, PublicityDomain updated) {
        if (id == null || updated == null) {
            throw new IllegalArgumentException("Datos inválidos para actualizar publicidad");
        }

        Optional<PublicityDomain> existingOpt = publicityRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new NoSuchElementException("La publicidad no existe");
        }

        UUID campaignId = updated.getCampaign().getId();
        UUID productId = updated.getProduct().getProductId();

        Optional<Campaigns> campaignOpt = campaignRepository.findById(campaignId);
        Optional<ProductDomain> productOpt = productRepository.findById(productId);

        if (campaignOpt.isEmpty()) {
            throw new NoSuchElementException("La campaña asociada no existe");
        }

        if (productOpt.isEmpty()) {
            throw new NoSuchElementException("El producto asociado no existe");
        }

        Campaigns campaign = campaignOpt.get();

        if (!updated.getStartDate().isEqual(campaign.getStartDate()) ||
                !updated.getEndDate().isEqual(campaign.getEndDate())) {
            throw new IllegalArgumentException("Las fechas de la publicidad deben coincidir exactamente con las fechas de la campaña");
        }

        PublicityDomain existing = existingOpt.get();
        existing.setCampaign(campaign);
        existing.setRole(updated.getRole());
        existing.setMessage(updated.getMessage());
        existing.setFrequencyAmount(updated.getFrequencyAmount());
        existing.setFrequency(updated.getFrequency());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());

        return publicityRepository.save(existing);
    }

    public void deletePublicity(UUID id) {
        if (id == null || !publicityRepository.existsById(id)) {
            throw new RuntimeException("No se encontró la factura con ID: " + id);
        }
        publicityRepository.deleteById(id);

    }
}
