package mybill.bank.co.payment_service.infrastructure.persistence.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import mybill.bank.co.payment_service.infrastructure.persistence.entities.PaymentTransactionEntity;

public interface JpaPaymentRepository extends JpaRepository<PaymentTransactionEntity, UUID> {

    Optional<PaymentTransactionEntity> findByPaymentReference(String externalId);

}
