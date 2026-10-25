package com.swiftyquote.notification.repository;

import com.swiftyquote.notification.entity.NotificationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationRepository extends JpaRepository<NotificationRecord,Long>{}
