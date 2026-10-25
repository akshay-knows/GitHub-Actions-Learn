package com.swiftyquote.qms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
public class QuotationItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 public String productName;
 public int quantity;
 public BigDecimal unitPrice;
 public BigDecimal lineTotal;
 // Child owns the foreign key; ignore back reference to avoid JSON recursion.
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="quotation_id") @JsonIgnore
 public Quotation quotation;
}
