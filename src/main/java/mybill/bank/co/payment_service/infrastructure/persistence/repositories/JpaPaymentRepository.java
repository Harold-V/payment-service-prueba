package mybill.bank.co.payment_service.infrastructure.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mybill.bank.co.payment_service.infrastructure.persistence.entities.PaymentTransactionEntity;

public interface JpaPaymentRepository extends JpaRepository<PaymentTransactionEntity, String> {

    Optional<PaymentTransactionEntity> findByPaymentReference(String externalId);

    Optional<PaymentTransactionEntity> findByPayerId(String payerId);

    Optional<PaymentTransactionEntity> findByInvoiceId(String invoiceId);

}
