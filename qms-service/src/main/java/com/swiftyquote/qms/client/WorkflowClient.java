package com.swiftyquote.qms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
// Feign turns this Java interface into an HTTP client.
@FeignClient(name="workflow-service", url="${workflow.url:http://localhost:8082}")
public interface WorkflowClient {
 @PostMapping("/api/approvals") ApprovalReply create(@RequestBody ApprovalRequest request);
 record ApprovalRequest(Long quotationId, String requestedBy) {}
 record ApprovalReply(Long approvalId,Long quotationId,String status) {}
}
