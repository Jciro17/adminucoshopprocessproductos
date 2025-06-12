package com.adminucoshopprocessproductos.adminucoshopprocessproductos.service.notification;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.notification.NotificationDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.UserDomain;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.notification.NotificationRepository;
import com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.userRegister.UserRegisterRepository;
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
    private final UserRegisterRepository userRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, UserRegisterRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void saveNotification(NotificationDomain notification) {
        validateNotification(notification);

        UUID userId = notification.getUser().getUserId();
        UserDomain user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado en el procesador"));

        notification.setUser(user);
        notification.setDateNotification(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public void deleteNotification(UUID id) {
        if (!notificationRepository.existsById(id)) {
            throw new IllegalArgumentException("La notificación a eliminar no existe.");
        }
        notificationRepository.deleteById(id);
    }

    public List<NotificationDomain> findByUser(UUID userId) {
        return notificationRepository.findByUserUserId(userId);
    }

    private void validateNotification(NotificationDomain notification) {
        if (notification.getTitle() == null || notification.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }
        if (notification.getProcess() == null || notification.getProcess().trim().isEmpty()) {
            throw new IllegalArgumentException("El proceso es obligatorio.");
        }
        if (notification.getAction() == null || notification.getAction().trim().isEmpty()) {
            throw new IllegalArgumentException("La acción es obligatoria.");
        }
        if (notification.getUser() == null || notification.getUser().getUserId() == null) {
            throw new IllegalArgumentException("El usuario de la notificación es obligatorio.");
        }
    }
}