package mybill.bank.co.payment_service.application.ports.in;

import java.util.Optional;

import mybill.bank.co.payment_service.domain.model.PaymentTransaction;

public interface PaymentTransactionRepositoryPort {
    PaymentTransaction save(PaymentTransaction payment);

    Optional<PaymentTransaction> findById(String id);

    Optional<PaymentTransaction> findByPaymentReference(String externalId);

    Optional<PaymentTransaction> findByPayerId(String payerId);

    Optional<PaymentTransaction> findByInvoiceId(String invoiceId);
}
