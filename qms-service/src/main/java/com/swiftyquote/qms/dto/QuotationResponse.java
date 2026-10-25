package com.swiftyquote.qms.dto;

import java.math.BigDecimal;
public record QuotationResponse(Long id,String customerName,BigDecimal totalAmount,String status) {}
