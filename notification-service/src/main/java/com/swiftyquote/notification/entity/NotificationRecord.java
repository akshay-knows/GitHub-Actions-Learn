package com.swiftyquote.notification.entity;

import jakarta.persistence.*;
import java.time.Instant;
@Entity public class NotificationRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public Long quotationId;
 public String eventType;
 public String message;
 public String deliveryStatus="MOCK_SENT";
 public Instant createdAt=Instant.now();
}
