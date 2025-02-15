package com.example.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "notification_template", schema = "notification")
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    private NotificationType notificationType;

    @Column(name = "subject")
    private String subject;

    @Column(name = "file_name")
    private String fileName;

    @Enumerated(EnumType.ORDINAL)
    private ProtocolType protocolType;
}
