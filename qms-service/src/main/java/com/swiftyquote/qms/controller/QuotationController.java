package com.swiftyquote.qms.controller;

import com.swiftyquote.qms.dto.*;
import com.swiftyquote.qms.service.QuotationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/quotations")
public class QuotationController {
 private final QuotationService service;
 public QuotationController(QuotationService service){this.service=service;}
 @PostMapping public ResponseEntity<QuotationResponse> create(@Valid @RequestBody QuotationRequest request){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));}
 @GetMapping public Page<QuotationResponse> list(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size){return service.list(page,size);}
 @GetMapping("/{id}") public QuotationResponse get(@PathVariable long id){return service.get(id);}
 @GetMapping("/{id}/preview") public java.util.Map<String,Object> preview(@PathVariable long id){return service.preview(id);}
 @PostMapping("/{id}/submit-approval") public QuotationResponse submit(@PathVariable long id){return service.submit(id);}
 // Workflow calls this internal endpoint after manager approves/rejects.
 @PostMapping("/{id}/decision") public QuotationResponse decision(@PathVariable long id,@RequestBody Decision request){return service.applyDecision(id,request.status());}
 public record Decision(String status) {}
}
