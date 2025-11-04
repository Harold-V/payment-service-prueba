package mybill.bank.co.payment_service.domain.ports;

import java.util.Optional;
import java.util.UUID;

import mybill.bank.co.payment_service.domain.model.PaymentTransaction;

public interface PaymentTransactionRepositoryPort {
    PaymentTransaction save(PaymentTransaction payment);

    Optional<PaymentTransaction> findById(UUID id);

    Optional<PaymentTransaction> findByPaymentReference(String reference);

}
