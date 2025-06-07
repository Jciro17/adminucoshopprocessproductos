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
                            BankRepository bankRepository,
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

    public ResponseEntity<Campaigns> findById(UUID id) {
        Campaigns campaign = campaignsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));
        return ResponseEntity.ok(campaign);
    }

    public ResponseEntity<String> saveCampaign(Campaigns campaign) {
        if (campaign == null) {
            return ResponseEntity.badRequest().body("La campaña no puede ser nula");
        }

        if (campaign.getId() != null && campaignsRepository.existsById(campaign.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El Id ya existe");
        }

        try {
            enrichAndValidateCampaign(campaign);
            campaign.setId(UUID.randomUUID());
            campaignsRepository.save(campaign);
            return ResponseEntity.ok("Campaña registrada exitosamente");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    public ResponseEntity<String> updateCampaign(UUID id, Campaigns updatedCampaign) {
        if (id == null || updatedCampaign == null) {
            return ResponseEntity.badRequest().body("Datos inválidos");
        }

        Campaigns existing = campaignsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campaña no encontrada"));

        try {
            enrichAndValidateCampaign(updatedCampaign);
            updatedCampaign.setId(id);
            campaignsRepository.save(updatedCampaign);
            return ResponseEntity.ok("Campaña actualizada exitosamente");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    public ResponseEntity<String> deleteCampaign(UUID id) {
        if (id == null || !campaignsRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La campaña no existe");
        }
        campaignsRepository.deleteById(id);
        return ResponseEntity.ok("Campaña eliminada exitosamente");
    }

    // ===================== MÉTODOS PRIVADOS =====================

    private void enrichAndValidateCampaign(Campaigns campaign) {
        validateDates(campaign);
        campaign.setDuration(String.valueOf(ChronoUnit.DAYS.between(campaign.getStartDate(), campaign.getEndDate())));
        validateApplicableFields(campaign);

        UUID userId = Optional.ofNullable(campaign.getUser())
                .map(UserDomain::getUserId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario obligatorio"));

        UserDomain user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        campaign.setUser(user);

        campaign.setProduct(validateProduct(campaign.getProduct()));
        campaign.setCategory(validateCategory(campaign.getCategory()));
        campaign.setBank(validateBank(campaign.getBank()));
        campaign.setDocumentType(validateDocumentType(campaign.getDocumentType()));
        campaign.setRole(validateRole(campaign.getRole()));
    }

    private void validateDates(Campaigns campaign) {
        if (campaign.getStartDate() == null || campaign.getEndDate() == null) {
            throw new IllegalArgumentException("Fechas de inicio y fin son obligatorias");
        }
        if (!campaign.getEndDate().isAfter(campaign.getStartDate())) {
            throw new IllegalArgumentException("Fecha de fin debe ser posterior a la de inicio");
        }
    }

    private void validateApplicableFields(Campaigns campaign) {
        boolean hasValidField = (campaign.getProduct() != null && campaign.getProduct().getProductId() != null)
                || (campaign.getCategory() != null && campaign.getCategory().getCategoryId() != null)
                || (campaign.getZone() != null && !campaign.getZone().isBlank())
                || (campaign.getBank() != null && campaign.getBank().getIdBank() != null)
                || (campaign.getDocumentType() != null && campaign.getDocumentType().getTypeOfDocumentId() > 0)
                || (campaign.getRole() != null && campaign.getRole().getRolId() > 0);

        if (!hasValidField) {
            throw new IllegalArgumentException("Debe proporcionar al menos un campo aplicable (producto, categoría, zona, banco, tipo de documento o rol)");
        }
    }

    private ProductDomain validateProduct(ProductDomain product) {
        if (product == null || product.getProductId() == null) return null;
        return productRepository.findById(product.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
    }

    private CategoryDomain validateCategory(CategoryDomain category) {
        if (category == null || category.getCategoryId() == null) return null;
        return categoryRepository.findById(category.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
    }

    private BankDomain validateBank(BankDomain bank) {
        if (bank == null || bank.getIdBank() == null) return null;
        return bankRepository.findById(bank.getIdBank())
                .orElseThrow(() -> new EntityNotFoundException("Banco no encontrado"));
    }

    private TypeOfDocumentDomain validateDocumentType(TypeOfDocumentDomain type) {
        if (type == null || type.getTypeOfDocumentId() <= 0) return null;
        boolean exists = documentTypeRepository.findAll().stream()
                .anyMatch(doc -> doc.getTypeOfDocumentId() == type.getTypeOfDocumentId());
        if (!exists) throw new EntityNotFoundException("Tipo de documento no encontrado");
        return type;
    }

    private RolDomain validateRole(RolDomain role) {
        if (role == null || role.getRolId() <= 0) return null;
        boolean exists = roleRepository.findAll().stream()
                .anyMatch(r -> r.getRolId() == role.getRolId());
        if (!exists) throw new EntityNotFoundException("Rol no encontrado");
        return role;
    }
}