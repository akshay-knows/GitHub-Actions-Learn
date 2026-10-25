package com.swiftyquote.notification.controller;

import com.swiftyquote.notification.entity.NotificationRecord;
import com.swiftyquote.notification.repository.NotificationRepository;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/notifications") public class NotificationController {
 private final NotificationRepository repo;
 public NotificationController(NotificationRepository repo){this.repo=repo;}
 @GetMapping public Page<NotificationRecord> list(@RequestParam(defaultValue="0")int page,@RequestParam(defaultValue="10")int size){
  if(page<0||size<1||size>100)throw new IllegalArgumentException("Invalid pagination");
  return repo.findAll(PageRequest.of(page,size,Sort.by("id").descending()));
 }
}
