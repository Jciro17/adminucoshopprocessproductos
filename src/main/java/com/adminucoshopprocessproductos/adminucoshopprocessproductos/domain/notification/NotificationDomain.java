package com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.notification;

import com.adminucoshopprocessproductos.adminucoshopprocessproductos.domain.user.UserDomain;
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDomain user;


    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "process", nullable = false)
    private String process;

    @Column(name = "date_Notification")
    private LocalDateTime dateNotification;

    @Column(name = "action", nullable = false)
    private String action;

    private static NotificationDomain instance;

    protected NotificationDomain() {}

    public static NotificationDomain getInstance() {
        if (instance == null) {
            instance = new NotificationDomain();
        }
        return instance;
    }

    public NotificationDomain(String title,UserDomain user, String process, String action) {
        this.title = title;
        this.user = user;
        this.process = process;
        this.dateNotification = LocalDateTime.now();
        this.action = action;
    }


}
