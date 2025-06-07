package com.adminucoshopprocessproductos.adminucoshopprocessproductos.repository.notification;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.notification.NotificationDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationDomain, UUID> {

}
