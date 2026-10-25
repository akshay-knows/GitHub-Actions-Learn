package com.swiftyquote.qms.dto;

import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
public record QuotationRequest(
 @NotNull Long customerId,
 @NotBlank String customerName,
 @NotEmpty List<@Valid Line> items) {
 public record Line(@NotBlank String productName,@Min(1) int quantity,@NotNull @DecimalMin("0.01") BigDecimal unitPrice) {}
}
