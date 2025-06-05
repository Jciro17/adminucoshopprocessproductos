package com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.campaign;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.campaign.CampaignsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.campaign.Campaigns;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.category.CategoryDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.payment_management.BankDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.product_management.ProductDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.RolDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.TypeOfDocumentDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.UserDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.campaign.RolRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.campaign.TypeDocumentRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.category.CategoryRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.payment_management.BankRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.product_management.ProductRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.userRegister.UserRegisterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
public class CampaignsService {

    private final CampaignsRepository campaignsRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRegisterRepository userRepository;
    private final BankRepository bankRepository;
    private final TypeDocumentRepository documentTypeRepository;
    private final RolRepository roleRepository;

    @Autowired
    public CampaignsService(CampaignsRepository campaignsRepository,
                            ProductRepository productRepository,
                            CategoryRepository categoryRepository,
                            UserRegisterRepository userRepository,
                            ObjectMapper objectMapper, BankRepository bankRepository,
                            TypeDocumentRepository documentTypeRepository,
                            RolRepository roleRepository) {
        this.campaignsRepository = campaignsRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
        this.documentTypeRepository = documentTypeRepository;
        this.roleRepository = roleRepository;
    }



    public List<Campaigns> findAll() {
        return campaignsRepository.findAll();
    }

    public ResponseEntity<Campaigns> findById(UUID campaignId) {
        if (campaignId == null) {
            throw new RuntimeException("EL Id de la campaña no puede ser nulo");
        }
        if (!campaignsRepository.existsById(campaignId)) {
            throw new EntityNotFoundException("La campaña no existe no existe");
        }

        Campaigns campaingns = campaignsRepository.findById(campaignId).orElse(null);
        return new ResponseEntity<>(campaingns, HttpStatus.OK);
    }

    public ResponseEntity<String> saveCampaign(Campaigns campaign) {
        if (campaign == null) {
            return ResponseEntity.badRequest().body("La campaña no puede ser nula");
        }

        if (campaign.getId() != null && campaignsRepository.existsById(campaign.getId())) {
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
                    .anyMatch((d -> d.getTypeOfDocumentId() == campaign.getDocumentType().getTypeOfDocumentId()))) {
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

        campaignsRepository.save(campaign);
        return ResponseEntity.ok("Campaña registrada exitosamente");
    }



    public ResponseEntity<String> deleteCampaign(UUID id) {
        if (id == null || !campaignsRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La campaña no existe");
        }
        campaignsRepository.deleteById(id);
        return ResponseEntity.ok("Campaña eliminada exitosamente");
    }

    public ResponseEntity<String> updateCampaign(UUID id, Campaigns updatedCampaign) {
        if (id == null || updatedCampaign == null) {
            return ResponseEntity.badRequest().body("Datos inválidos para actualizar campaña");
        }

        Campaigns existingCampaign = campaignsRepository.findById(id).orElse(null);
        if (existingCampaign == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La campaña no existe");
        }

        // Validar usuario
        if (updatedCampaign.getUser() == null || updatedCampaign.getUser().getUserId() == null) {
            return ResponseEntity.badRequest().body("El usuario es obligatorio");
        }
        UUID userId = updatedCampaign.getUser().getUserId();
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario no existe");
        }
        UserDomain user = userRepository.findById(userId).orElseThrow();

        // Validar fechas
        if (updatedCampaign.getStartDate() == null || updatedCampaign.getEndDate() == null) {
            return ResponseEntity.badRequest().body("Las fechas de inicio y fin son obligatorias");
        }
        if (!updatedCampaign.getEndDate().isAfter(updatedCampaign.getStartDate())) {
            return ResponseEntity.badRequest().body("La fecha de fin debe ser mayor a la fecha de inicio");
        }

        // Validar al menos uno de los campos opcionales
        boolean hasValidField = (updatedCampaign.getProduct() != null && updatedCampaign.getProduct().getProductId() != null) ||
                (updatedCampaign.getCategory() != null && updatedCampaign.getCategory().getCategoryId() != null) ||
                (updatedCampaign.getZone() != null && !updatedCampaign.getZone().isBlank()) ||
                (updatedCampaign.getBank() != null && updatedCampaign.getBank().getIdBank() != null) ||
                (updatedCampaign.getDocumentType() != null && updatedCampaign.getDocumentType().getTypeOfDocumentId() > 0) ||
                (updatedCampaign.getRole() != null && updatedCampaign.getRole().getRolId() > 0);

        if (!hasValidField) {
            return ResponseEntity.badRequest().body("Debe existir al menos un campo válido: producto, categoría, zona, banco, tipo de documento o rol");
        }

        // Validar y asignar campos relacionados
        if (updatedCampaign.getProduct() != null && updatedCampaign.getProduct().getProductId() != null) {
            UUID productId = updatedCampaign.getProduct().getProductId();
            if (!productRepository.existsById(productId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
            }
            existingCampaign.setProduct(productRepository.findById(productId).orElseThrow());
        }

        if (updatedCampaign.getCategory() != null && updatedCampaign.getCategory().getCategoryId() != null) {
            UUID categoryId = updatedCampaign.getCategory().getCategoryId();
            if (!categoryRepository.existsById(categoryId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoría no existe");
            }
            existingCampaign.setCategory(categoryRepository.findById(categoryId).orElseThrow());
        }

        if (updatedCampaign.getBank() != null && updatedCampaign.getBank().getIdBank() != null) {
            UUID bankId = updatedCampaign.getBank().getIdBank();
            if (!bankRepository.existsById(bankId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El banco no existe");
            }
            existingCampaign.setBank(bankRepository.findById(bankId).orElseThrow());
        }

        if (updatedCampaign.getDocumentType() != null && updatedCampaign.getDocumentType().getTypeOfDocumentId() > 0) {
            int docTypeId = updatedCampaign.getDocumentType().getTypeOfDocumentId();
            Optional<TypeOfDocumentDomain> documentType = documentTypeRepository.findAll().stream()
                    .filter(d -> d.getTypeOfDocumentId() == docTypeId)
                    .findFirst();
            if (documentType.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tipo de documento no existe");
            }
            existingCampaign.setDocumentType(documentType.get());
        }

        if (updatedCampaign.getRole() != null && updatedCampaign.getRole().getRolId() > 0) {
            int roleId = updatedCampaign.getRole().getRolId();
            Optional<RolDomain> role = roleRepository.findAll().stream()
                    .filter(r -> r.getRolId() == roleId)
                    .findFirst();
            if (role.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El rol especificado no existe");
            }
            existingCampaign.setRole(role.get());
        }

        // Asignar datos simples
        existingCampaign.setUser(user);
        existingCampaign.setZone(updatedCampaign.getZone());
        existingCampaign.setStartDate(updatedCampaign.getStartDate());
        existingCampaign.setEndDate(updatedCampaign.getEndDate());
        existingCampaign.setRegistrationDate(updatedCampaign.getRegistrationDate());

        // Calcular duración
        long duration = ChronoUnit.DAYS.between(updatedCampaign.getStartDate(), updatedCampaign.getEndDate());
        existingCampaign.setDuration(String.valueOf(duration));

        // Guardar cambios
        campaignsRepository.save(existingCampaign);
        return ResponseEntity.ok("Campaña actualizada exitosamente");
    }

}