package com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.notification;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.notification.NotificationDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationDomain, UUID> {

}