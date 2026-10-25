package com.swiftyquote.qms.service;

import com.swiftyquote.qms.client.WorkflowClient;
import com.swiftyquote.qms.dto.*;
import com.swiftyquote.qms.entity.*;
import com.swiftyquote.qms.repository.QuotationRepository;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class QuotationService {
 private static final Logger log=LoggerFactory.getLogger(QuotationService.class);
 private final QuotationRepository repository;
 private final WorkflowClient workflow;
 public QuotationService(QuotationRepository repository,WorkflowClient workflow){this.repository=repository;this.workflow=workflow;}
 @Transactional
 public QuotationResponse create(QuotationRequest request){
  Quotation quotation=new Quotation();quotation.customerId=request.customerId();quotation.customerName=request.customerName();
  for(var line:request.items()){
   QuotationItem item=new QuotationItem();item.productName=line.productName();item.quantity=line.quantity();item.unitPrice=line.unitPrice();
   item.lineTotal=line.unitPrice().multiply(BigDecimal.valueOf(line.quantity()));
   quotation.addItem(item);quotation.totalAmount=quotation.totalAmount.add(item.lineTotal);
  }
  Quotation saved=repository.save(quotation);
  log.info("quotation-created id={} customerId={} total={}",saved.id,saved.customerId,saved.totalAmount);
  return convert(saved);
 }
 @Transactional(readOnly=true)
 public Page<QuotationResponse> list(int page,int size){
  // Limit page size; pagination stops APIs from fetching unlimited records.
  if(page<0 || size<1 || size>100) throw new IllegalArgumentException("page must be >= 0 and size 1..100");
  return repository.findAll(PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"id"))).map(this::convert);
 }
 @Transactional(readOnly=true)
 public QuotationResponse get(long id){return convert(find(id));}
 @Transactional(readOnly=true)
 public java.util.Map<String,Object> preview(long id){
  Quotation q=find(id);
  // Business feature: show an easy-to-read quotation preview with items.
  return java.util.Map.of("quotationId",q.id,"customer",q.customerName,"items",q.items.stream().map(i->java.util.Map.of("product",i.productName,"quantity",i.quantity,"unitPrice",i.unitPrice,"lineTotal",i.lineTotal)).toList(),"grandTotal",q.totalAmount,"status",q.status);
 }
 // Separate remote calls from DB transactions: a timeout must not hold a DB connection open.
 public QuotationResponse submit(long id){
  Quotation q=find(id);
  if(!"DRAFT".equals(q.status))throw new IllegalArgumentException("Only DRAFT quotations can be submitted");
  log.info("approval-submit-start quotationId={}",id);
  var result=workflow.create(new WorkflowClient.ApprovalRequest(id,"sales-executive"));
  log.info("approval-request-created quotationId={} approvalId={}",id,result.approvalId());
  return markSubmitted(id);
 }
 @Transactional
 public QuotationResponse markSubmitted(long id){
  Quotation q=find(id);if(!"DRAFT".equals(q.status))throw new IllegalArgumentException("Quotation already submitted");
  q.status="PENDING_APPROVAL";log.info("quotation-status-change id={} status={}",id,q.status);return convert(q);
 }
 @Transactional
 public QuotationResponse applyDecision(long id,String decision){
  Quotation q=find(id);
  if(!"PENDING_APPROVAL".equals(q.status))throw new IllegalArgumentException("Quotation is not pending approval");
  if(!"APPROVED".equals(decision) && !"REJECTED".equals(decision))throw new IllegalArgumentException("Invalid decision");
  q.status=decision;log.info("quotation-decision id={} status={}",id,decision);return convert(q);
 }
 private Quotation find(long id){return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Quotation not found: "+id));}
 private QuotationResponse convert(Quotation q){return new QuotationResponse(q.id,q.customerName,q.totalAmount,q.status);}
}
