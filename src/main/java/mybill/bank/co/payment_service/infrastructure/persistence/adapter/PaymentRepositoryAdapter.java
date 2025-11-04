package mybill.bank.co.payment_service.infrastructure.persistence.adapter;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import mybill.bank.co.payment_service.domain.model.PaymentTransaction;
import mybill.bank.co.payment_service.domain.ports.PaymentTransactionRepositoryPort;
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
    public Optional<PaymentTransaction> findById(UUID id) {
        return jpaPaymentRepository.findById(id)
                .map(paymentMapper::toDomain);
    }

    @Override
    public Optional<PaymentTransaction> findByPaymentReference(String reference) {
        return jpaPaymentRepository.findByPaymentReference(reference)
                .map(paymentMapper::toDomain);
    }

}
