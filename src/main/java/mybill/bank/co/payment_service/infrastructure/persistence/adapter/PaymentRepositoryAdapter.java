package mybill.bank.co.payment_service.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mybill.bank.co.payment_service.application.ports.in.PaymentTransactionRepositoryPort;
import mybill.bank.co.payment_service.domain.model.PaymentTransaction;
import mybill.bank.co.payment_service.infrastructure.persistence.entities.PaymentTransactionEntity;
import mybill.bank.co.payment_service.infrastructure.persistence.mappers.PaymentMapper;
import mybill.bank.co.payment_service.infrastructure.persistence.repositories.JpaPaymentRepository;

@RequiredArgsConstructor
@Component
public class PaymentRepositoryAdapter implements PaymentTransactionRepositoryPort {

    private final JpaPaymentRepository jpaPaymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentTransaction save(PaymentTransaction payment) {
        PaymentTransactionEntity entity = paymentMapper.toEntity(payment);
        PaymentTransactionEntity savedEntity = jpaPaymentRepository.save(entity);
        return paymentMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<PaymentTransaction> findById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public Optional<PaymentTransaction> findByPaymentReference(String externalId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByPaymentReference'");
    }

    @Override
    public Optional<PaymentTransaction> findByPayerId(String payerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByPayerId'");
    }

    @Override
    public Optional<PaymentTransaction> findByInvoiceId(String invoiceId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByInvoiceId'");
    }

}
