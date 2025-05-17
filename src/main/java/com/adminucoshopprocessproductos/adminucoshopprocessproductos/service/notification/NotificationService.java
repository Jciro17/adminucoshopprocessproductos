package com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.notification;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.notification.NotificationDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void sendProductAddedNotification(String productName) {
        NotificationDomain notification = new NotificationDomain();
        notification.setTitle("Nuevo producto agregado");
        notification.setProcess("Gestión de productos");
        notification.setAction("Se ha agregado el producto: " + productName);
        notification.setDateNotification(LocalDateTime.now());

        saveNotification(notification);
    }

    public List<NotificationDomain> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public NotificationDomain getNotificationById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID es obligatorio.");
        }
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La notificación no existe."));
    }

    public void saveNotification(NotificationDomain notification) {
        if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }
        if (notification.getProcess() == null || notification.getProcess().trim().isEmpty()) {
            throw new IllegalArgumentException("El proceso es obligatorio.");
        }
        if (notification.getAction() == null || notification.getAction().trim().isEmpty()) {
            throw new IllegalArgumentException("La acción es obligatoria.");
        }
        notification.setDateNotification(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public void deleteNotification(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID es obligatorio.");
        }
        if (!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("La notificación a eliminar no existe.");
        }
        notificationRepository.deleteById(id);
    }
}
