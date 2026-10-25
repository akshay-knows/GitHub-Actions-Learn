package com.swiftyquote.workflow.controller;

import com.swiftyquote.workflow.entity.Approval;
import com.swiftyquote.workflow.service.ApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/approvals")
public class ApprovalController {
    private final ApprovalService service;

    public ApprovalController(ApprovalService service) {
        this.service = service;
    }

    public record NewApproval(Long quotationId, String requestedBy) {
    }

    public record Decision(String reason) {
    }

    @PostMapping

    public ResponseEntity<Approval> create(@RequestBody NewApproval request) {
        return ResponseEntity.status(201)
                             .body(service.create(
                                     request.quotationId(),
                                     request.requestedBy()
                             ));
    }

    @GetMapping("/{id}")
    public Approval find(@PathVariable long id) {
        return service.find(id);
    }

    @PostMapping("/{id}/approve")
    public Approval approve(@PathVariable long id) {
        return service.decide(
                id,
                "APPROVED",
                null
        );
    }

    @PostMapping("/{id}/reject")
    public Approval reject(@PathVariable long id, @RequestBody Decision request) {
        return service.decide(
                id,
                "REJECTED",
                request.reason()
        );
    }
}
