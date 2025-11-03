package mybill.bank.co.payment_service.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;

import mybill.bank.co.payment_service.domain.model.PaymentTransaction;
import mybill.bank.co.payment_service.infrastructure.persistence.entities.PaymentTransactionEntity;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentTransaction toDomain(PaymentTransactionEntity entity);

    PaymentTransactionEntity toEntity(PaymentTransaction domain);
}
