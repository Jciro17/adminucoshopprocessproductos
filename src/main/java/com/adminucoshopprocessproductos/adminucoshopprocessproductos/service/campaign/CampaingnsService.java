package com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.campaign;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.campaign.Campaingns;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.category.CategoryDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.payment_management.BankDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.product_management.ProductDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.RolDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.TypeOfDocumentDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.UserDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.campaign.CampaingnsRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.campaign.RolRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.campaign.TypeDocumentRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.category.CategoryRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.payment_management.BankRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.product_management.ProductRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.userRegister.UserRegisterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
public class CampaingnsService {

    private final CampaingnsRepository campaingnsRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRegisterRepository userRepository;
    private final BankRepository bankRepository;
    private final TypeDocumentRepository documentTypeRepository;
    private final RolRepository roleRepository;

    @Autowired
    public CampaingnsService(CampaingnsRepository campaingnsRepository,
                             ProductRepository productRepository,
                             CategoryRepository categoryRepository,
                             UserRegisterRepository userRepository,
                             ObjectMapper objectMapper, BankRepository bankRepository,
                             TypeDocumentRepository documentTypeRepository,
                             RolRepository roleRepository) {
        this.campaingnsRepository = campaingnsRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.roleRepository = roleRepository;
    }



    public List<Campaingns> findAll() {
        return campaingnsRepository.findAll();
    }

    public ResponseEntity<Campaingns> findById(UUID campaignId) {
        if (campaignId == null) {
            throw new RuntimeException("EL Id de la campaña no puede ser nulo");
        }
        if (!campaingnsRepository.existsById(campaignId)) {
            throw new EntityNotFoundException("La campaña no existe no existe");
        }

        Campaingns campaingns = campaingnsRepository.findById(campaignId).orElse(null);
        return new ResponseEntity<>(campaingns, HttpStatus.OK);
    }

    public ResponseEntity<String> saveCampaign(Campaingns campaign) {
        if (campaign == null) {
            return ResponseEntity.badRequest().body("La campaña no puede ser nula");
        }

        if (campaign.getId() != null && campaingnsRepository.existsById(campaign.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Id de la campaña ya existe");
        }

        // Validar usuario
        if (campaign.getUser() == null || campaign.getUser().getUserId() == null) {
            return ResponseEntity.badRequest().body("El usuario es obligatorio");
        }
        if (!userRepository.existsById(campaign.getUser().getUserId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario no existe");
        }
        UserDomain user = userRepository.findById(campaign.getUser().getUserId()).orElseThrow();

        // Validar fechas
        if (campaign.getStartDate() == null || campaign.getEndDate() == null) {
            return ResponseEntity.badRequest().body("Las fechas de inicio y fin son obligatorias");
        }
        if (!campaign.getEndDate().isAfter(campaign.getStartDate())) {
            return ResponseEntity.badRequest().body("La fecha de fin debe ser mayor a la fecha de inicio");
        }

        // Calcular duración
        long duration = ChronoUnit.DAYS.between(campaign.getStartDate(), campaign.getEndDate());
        campaign.setDuration(String.valueOf(duration));

        // Validar al menos uno de los campos aplicables
        boolean hasValidField = (campaign.getProduct() != null && campaign.getProduct().getProductId() != null) ||
                (campaign.getCategory() != null && campaign.getCategory().getCategoryId() != null) ||
                (campaign.getZone() != null && !campaign.getZone().isBlank()) ||
                (campaign.getBank() != null && campaign.getBank().getIdBank() != null) ||
                (campaign.getDocumentType() != null && campaign.getDocumentType().getTypeOfDocumentId() > 0) ||
                (campaign.getRole() != null && campaign.getRole().getRolId() > 0);

        if (!hasValidField) {
            return ResponseEntity.badRequest().body("Debe existir al menos un campo válido: producto, categoría, zona, banco, tipo de documento o rol");
        }

        // Validar producto
        ProductDomain product = null;
        if (campaign.getProduct() != null && campaign.getProduct().getProductId() != null) {
            if (!productRepository.existsById(campaign.getProduct().getProductId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
            }
            product = productRepository.findById(campaign.getProduct().getProductId()).orElseThrow();
        }

        // Validar categoría
        CategoryDomain category = null;
        if (campaign.getCategory() != null && campaign.getCategory().getCategoryId() != null) {
            if (!categoryRepository.existsById(campaign.getCategory().getCategoryId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoría no existe");
            }
            category = categoryRepository.findById(campaign.getCategory().getCategoryId()).orElseThrow();
        }

        // Validar banco
        BankDomain bank = null;
        if (campaign.getBank() != null && campaign.getBank().getIdBank() != null) {
            if (!bankRepository.existsById(campaign.getBank().getIdBank())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El banco no existe");
            }
            bank = bankRepository.findById(campaign.getBank().getIdBank()).orElseThrow();
        }

        // Validar tipo de documento
        TypeOfDocumentDomain documentType = null;
        if (campaign.getDocumentType() != null && campaign.getDocumentType().getTypeOfDocumentId() > 0) {
            if (!documentTypeRepository.findAll().stream()
                    .noneMatch(d -> d.getTypeOfDocumentId() == campaign.getDocumentType().getTypeOfDocumentId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tipo de documento no existe");
            }
            documentType = campaign.getDocumentType();
        }

        // Validar rol
        RolDomain role = null;
        if (campaign.getRole() != null && campaign.getRole().getRolId() > 0) {
            if (roleRepository.findAll().stream()
                    .anyMatch(r -> r.getRolId() == campaign.getRole().getRolId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El rol especificado no existe");
            }
            role = campaign.getRole();
        }

        // Seteo final
        campaign.setId(UUID.randomUUID());
        campaign.setProduct(product);
        campaign.setCategory(category);
        campaign.setBank(bank);
        campaign.setDocumentType(documentType);
        campaign.setRole(role);
        campaign.setUser(user);

        campaingnsRepository.save(campaign);
        return ResponseEntity.ok("Campaña registrada exitosamente");
    }



    public ResponseEntity<String> deleteCampaign(UUID id) {
        if (id == null || !campaingnsRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La campaña no existe");
        }
        campaingnsRepository.deleteById(id);
        return ResponseEntity.ok("Campaña eliminada exitosamente");
    }

    public ResponseEntity<String> updateCampaign(UUID id, Campaingns updatedCampaign) {
        if (id == null || updatedCampaign == null) {
            return ResponseEntity.badRequest().body("Datos inválidos para actualizar campaña");
        }

        Campaingns existingCampaign = campaingnsRepository.findById(id).orElse(null);
        if (existingCampaign == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La campaña no existe");
        }

        // Actualización de relaciones (si no vienen como null)
        if (updatedCampaign.getProduct() != null) {
            UUID productId = updatedCampaign.getProduct().getProductId();
            existingCampaign.setProduct(productRepository.findById(productId).orElse(null));
        }

        if (updatedCampaign.getCategory() != null) {
            UUID categoryId = updatedCampaign.getCategory().getCategoryId();
            existingCampaign.setCategory(categoryRepository.findById(categoryId).orElse(null));
        }

        if (updatedCampaign.getUser() != null) {
            UUID userId = updatedCampaign.getUser().getUserId();
            existingCampaign.setUser(userRepository.findById(userId).orElse(null));
        }

        // Datos simples
        existingCampaign.setZone(updatedCampaign.getZone());
        existingCampaign.setRole(updatedCampaign.getRole());
        existingCampaign.setDocumentType(updatedCampaign.getDocumentType());
        existingCampaign.setBank(updatedCampaign.getBank());
        existingCampaign.setStartDate(updatedCampaign.getStartDate());
        existingCampaign.setEndDate(updatedCampaign.getEndDate());
        existingCampaign.setRegistrationDate(updatedCampaign.getRegistrationDate());

        // Cálculo automático de duración si ambas fechas están presentes
        if (updatedCampaign.getStartDate() != null && updatedCampaign.getEndDate() != null) {
            long daysBetween = ChronoUnit.DAYS.between(updatedCampaign.getStartDate(), updatedCampaign.getEndDate());
            existingCampaign.setDuration(String.valueOf((int) daysBetween));
        }

        campaingnsRepository.save(existingCampaign);
        return ResponseEntity.ok("Campaña actualizada exitosamente");
    }

}
