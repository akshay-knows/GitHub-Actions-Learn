package com.swiftyquote.workflow.entity;

import jakarta.persistence.*;
import java.time.Instant;
@Entity public class Approval {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(unique=true,nullable=false) public Long quotationId;
 public String requestedBy;
 public String status="PENDING";
 public String reason;
 public Instant createdAt=Instant.now();
}
