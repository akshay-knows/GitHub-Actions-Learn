package com.swiftyquote.qms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Quotation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public Long customerId;
 public String customerName;
 public String status = "DRAFT";
 public BigDecimal totalAmount = BigDecimal.ZERO;
 // Cascade saves all line items in the same transaction as the quotation.
 @OneToMany(mappedBy="quotation",cascade=CascadeType.ALL,orphanRemoval=true)
 public List<QuotationItem> items = new ArrayList<>();
 public void addItem(QuotationItem item) { item.quotation=this; items.add(item); }
}
