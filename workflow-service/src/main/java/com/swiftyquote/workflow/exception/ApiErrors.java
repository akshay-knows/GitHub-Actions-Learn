package com.swiftyquote.workflow.exception;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class ApiErrors {
    private static final Logger log = LoggerFactory.getLogger(ApiErrors.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> bad(IllegalArgumentException e) {
        return ResponseEntity.badRequest()
                             .body(Map.of(
                                     "error",
                                     e.getMessage()
                             ));
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<?> upstream(RestClientException e) {
        log.error(
                "qms-callback-failed",
                e
        );
        return ResponseEntity.status(503)
                             .body(Map.of(
                                     "error",
                                     "QMS callback failed; approval not completed"
                             ));
    }
}
