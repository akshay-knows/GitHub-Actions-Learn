package com.swiftyquote.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.swiftyquote.workflow.entity.Approval;
import java.util.Optional;
public interface ApprovalRepository extends JpaRepository<Approval,Long>{ Optional<Approval> findByQuotationId(Long quotationId); }
