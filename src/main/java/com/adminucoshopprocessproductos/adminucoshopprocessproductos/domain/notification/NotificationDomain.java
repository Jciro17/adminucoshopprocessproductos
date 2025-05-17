package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.notification;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "notifications")
public class NotificationDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_Notification", updatable = false, nullable = false)
    private UUID idNotification;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "process", nullable = false)
    private String process;

    @Column(name = "date_Notification")
    private LocalDateTime dateNotification;

    @Column(name = "action", nullable = false)
    private String action;

    public NotificationDomain() {
    }

    // Constructor con parámetros
    public NotificationDomain(String title, String process, String action) {
        this.title = title;
        this.process = process;
        this.dateNotification = LocalDateTime.now();
        this.action = action;
    }
}