package com.swiftyquote.qms.exception;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.http.*;import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import feign.FeignException;
import java.util.Map;
@RestControllerAdvice
public class ApiErrors {
 private static final Logger log=LoggerFactory.getLogger(ApiErrors.class);
 @ExceptionHandler(IllegalArgumentException.class) public ResponseEntity<?> bad(IllegalArgumentException e){log.warn("bad-request reason={}",e.getMessage());return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<?> validation(MethodArgumentNotValidException e){return ResponseEntity.badRequest().body(Map.of("error","Invalid quotation request","details",e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).toList()));}
 @ExceptionHandler(FeignException.class) public ResponseEntity<?> downstream(FeignException e){log.error("workflow-call-failed status={} message={}",e.status(),e.getMessage());return ResponseEntity.status(503).body(Map.of("error","Workflow service unavailable or returned an error"));}
}
