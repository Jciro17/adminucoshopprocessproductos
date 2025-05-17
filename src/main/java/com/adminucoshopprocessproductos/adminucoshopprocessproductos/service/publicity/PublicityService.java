package com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.publicity;


import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.product_management.ProductDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.publicity.PublicityDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.publicity.PublicityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PublicityService {

    private final PublicityRepository publicityRepository;
    private final CampaingnsRepository campaignRepository;
    private final ProductRepository productRepository;

    public PublicityService(
            PublicityRepository publicityRepository,
            CampaingnsRepository campaignRepository,
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

    public ResponseEntity<Object> createPublicity(PublicityDomain publicity) {
        if (publicity == null || publicity.getCampaign() == null || publicity.getProduct() == null) {
            return ResponseEntity.badRequest().body("Los datos de la publicidad no pueden ser nulos");
        }

        UUID campaignId = publicity.getCampaign().getId();
        UUID productId = publicity.getProduct().getProductId();

        Optional<Campaingns> campaignOpt = campaignRepository.findById(campaignId);
        Optional<ProductDomain> productOpt = productRepository.findById(productId);

        if (campaignOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La campaña asociada no existe");
        }

        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto asociado no existe");
        }

        Campaingns campaign = campaignOpt.get();

        if (!publicity.getStartDate().isEqual(campaign.getStartDate()) ||
                !publicity.getEndDate().isEqual(campaign.getEndDate())) {
            return ResponseEntity.badRequest().body("Las fechas de la publicidad deben coincidir exactamente con las fechas de la campaña");
        }

        publicity.setCampaign(campaign);
        publicity.setProduct(productOpt.get());

        PublicityDomain saved = publicityRepository.save(publicity);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    public ResponseEntity<Object> updatePublicity(UUID id, PublicityDomain updated) {
        if (id == null || updated == null) {
            return ResponseEntity.badRequest().body("Datos inválidos para actualizar publicidad");
        }

        Optional<PublicityDomain> existingOpt = publicityRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La publicidad no existe");
        }

        UUID campaignId = updated.getCampaign().getId();
        UUID productId = updated.getProduct().getProductId();

        Optional<Campaingns> campaignOpt = campaignRepository.findById(campaignId);
        Optional<ProductDomain> productOpt = productRepository.findById(productId);

        if (campaignOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La campaña asociada no existe");
        }

        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto asociado no existe");
        }

        Campaingns campaign = campaignOpt.get();

        if (!updated.getStartDate().isEqual(campaign.getStartDate()) ||
                !updated.getEndDate().isEqual(campaign.getEndDate())) {
            return ResponseEntity.badRequest().body("Las fechas de la publicidad deben coincidir exactamente con las fechas de la campaña");
        }

        PublicityDomain existing = existingOpt.get();
        existing.setCampaign(campaign);
        existing.setRole(updated.getRole());
        existing.setMessage(updated.getMessage());
        existing.setFrequencyAmount(updated.getFrequencyAmount());
        existing.setFrequency(updated.getFrequency());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());


        PublicityDomain saved = publicityRepository.save(existing);
        return ResponseEntity.ok(saved);
    }

    public ResponseEntity<String> deletePublicity(UUID id) {
        if (id == null || !publicityRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La publicidad no existe");
        }

        publicityRepository.deleteById(id);
        return ResponseEntity.ok("Publicidad eliminada exitosamente");
    }
}
