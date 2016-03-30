package com.example.myeventsourcing.financial.service.repo;

import com.example.myeventsourcing.financial.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrador on 14/03/2016.
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
