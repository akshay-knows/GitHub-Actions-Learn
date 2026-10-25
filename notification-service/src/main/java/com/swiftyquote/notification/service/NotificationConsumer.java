package com.swiftyquote.notification.service;

import com.swiftyquote.notification.entity.NotificationRecord;
import com.swiftyquote.notification.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service public class NotificationConsumer {
 private static final Logger log=LoggerFactory.getLogger(NotificationConsumer.class);
 private final NotificationRepository repo;
 public NotificationConsumer(NotificationRepository repo){this.repo=repo;}
 @KafkaListener(topics="quotation-decisions",groupId="notification-group")
 public void consume(String event){
  // Format: quotationId|APPROVED-or-REJECTED|optional reason.
  String[] parts=event.split("\\|",3);
  NotificationRecord n=new NotificationRecord();n.quotationId=Long.parseLong(parts[0]);n.eventType=parts[1];
  n.message="Quotation "+parts[0]+" was "+parts[1]+(parts.length>2&&!parts[2].isBlank()?". Reason: "+parts[2]:"");
  repo.save(n);log.info("mock-email-sent quotationId={} eventType={} message={}",n.quotationId,n.eventType,n.message);
 }
}
