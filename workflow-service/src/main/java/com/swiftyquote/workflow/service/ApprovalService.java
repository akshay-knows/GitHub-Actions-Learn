package com.swiftyquote.workflow.service;

import com.swiftyquote.workflow.entity.Approval;
import com.swiftyquote.workflow.repository.ApprovalRepository;
import java.util.Map;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
@Service public class ApprovalService {
 private static final Logger log=LoggerFactory.getLogger(ApprovalService.class);
 private final ApprovalRepository repo;private final KafkaTemplate<String,String> kafka;private final RestClient http=RestClient.create();
 @org.springframework.beans.factory.annotation.Value("${qms.url:http://localhost:8081}") private String qmsUrl;
 public ApprovalService(ApprovalRepository repo,KafkaTemplate<String,String> kafka){this.repo=repo;this.kafka=kafka;}
 public Approval create(Long quotationId,String requestedBy){
  if(repo.findByQuotationId(quotationId).isPresent())throw new IllegalArgumentException("Already requested");
  Approval a=new Approval();a.quotationId=quotationId;a.requestedBy=requestedBy;
  a=repo.save(a);log.info("approval-created id={} quotationId={}",a.id,quotationId);return a;
 }
 public Approval decide(long id,String status,String reason){
  Approval a=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Approval not found"));
  if(!"PENDING".equals(a.status))throw new IllegalArgumentException("Approval already decided");
  if(!"APPROVED".equals(status)&&!"REJECTED".equals(status))throw new IllegalArgumentException("Use APPROVED or REJECTED");
  if("REJECTED".equals(status)&&(reason==null||reason.isBlank()))throw new IllegalArgumentException("Rejection reason required");
  // Update QMS first; if that fails, leave approval PENDING for safe retry.
  http.post().uri(qmsUrl+"/api/quotations/"+a.quotationId+"/decision").body(Map.of("status",status)).retrieve().toBodilessEntity();
  a.status=status;a.reason=reason;a=repo.save(a);
  log.info("approval-decided id={} quotationId={} decision={}",a.id,a.quotationId,status);
  // Publish a simple pipe-delimited event so Kafka code remains easy to read.
  kafka.send("quotation-decisions",a.quotationId.toString(),a.quotationId+"|"+status+"|"+(reason==null?"":reason));
  return a;
 }
 public Approval find(long id){return repo.findById(id).orElseThrow(()->new IllegalArgumentException("Approval not found"));}
}
