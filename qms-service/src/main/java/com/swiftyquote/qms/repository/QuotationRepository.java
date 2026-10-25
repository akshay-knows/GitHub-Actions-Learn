package com.swiftyquote.qms.repository;

import com.swiftyquote.qms.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface QuotationRepository extends JpaRepository<Quotation,Long> {}
