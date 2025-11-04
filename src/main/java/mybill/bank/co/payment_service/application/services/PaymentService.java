package mybill.bank.co.payment_service.application.services;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybill.bank.co.payment_service.application.ports.in.PaymentUseCase;
import mybill.bank.co.payment_service.application.ports.out.TransactionWompiUseCase;
import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;
import mybill.bank.co.payment_service.domain.enumerations.PaymentProvider;
import mybill.bank.co.payment_service.domain.enumerations.TransactionStatus;
import mybill.bank.co.payment_service.domain.model.PaymentTransaction;
import mybill.bank.co.payment_service.domain.ports.PaymentTransactionRepositoryPort;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiWebHookResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements PaymentUseCase {

    private final TransactionWompiUseCase wompiGateway;
    private final PaymentTransactionRepositoryPort paymentRepository;

    // --- Helpers para construir/actualizar dominio ---
    private PaymentTransaction newPending(String invoiceId, String payerId, BigDecimal amount, CurrencyType currency,
            PaymentProvider provider, String reference) {
        ZonedDateTime now = ZonedDateTime.now();
        PaymentTransaction tx = new PaymentTransaction();
        tx.setTransactionId(UUID.randomUUID());
        tx.setExternalTransactionId(null);
        tx.setPaymentReference(reference);
        tx.setStatus(TransactionStatus.PENDING);
        tx.setPayerId(payerId);
        tx.setInvoiceId(invoiceId);
        tx.setAmount(amount);
        tx.setCurrencyType(currency);
        tx.setPaymentProvider(provider);
        tx.setResponseCode(null);
        tx.setRejectionCause(null);
        tx.setCreatedAt(now);
        tx.setUpdatedAt(now);
        return tx;
    }

    @Override
    @Transactional
    public WompiPaymentResponse createWompiPayment(WompiPaymentRequest request) {
        // 1) Llama a la pasarela para construir los datos de pago (ref, firma, etc.)
        WompiPaymentResponse response = wompiGateway.processPayment(request);

        // 2) Persistir una transacción PENDING (si ya existe por reference, evita
        // duplicar)
        String reference = response.reference();
        paymentRepository.findByPaymentReference(reference).ifPresentOrElse(existing -> {
            log.info("Transaction with reference {} already exists. Skipping create.", reference);
        }, () -> {
            // amountInCents -> BigDecimal en unidades
            BigDecimal amount = BigDecimal.valueOf(response.amountInCents()).movePointLeft(2);
            PaymentTransaction pending = newPending(
                    /* invoiceId */ request.invoiceId(),
                    /* payerId */ request.payerId(),
                    /* amount */ amount,
                    /* currency */ response.currency(),
                    /* provider */ PaymentProvider.WOMPI,
                    /* reference */ reference);
            paymentRepository.save(pending);
            log.info("Saved PENDING transaction for reference {}", reference);
        });

        return response;
    }

    @Override
    @Transactional
    public void handleWompiWebhook(WompiWebHookResponse webhook) {
        // 1) Tomamos transaction.id y reference desde el webhook
        var t = webhook.data().transaction();
        String reference = t.reference();
        String externalId = t.id();

        // 2) Buscamos la transacción por reference (creada en createWompiPayment)
        PaymentTransaction tx = paymentRepository.findByPaymentReference(reference)
                .orElseGet(() -> {
                    // Si por algún motivo el webhook llega primero, creamos registro mínimo
                    PaymentTransaction created = newPending(
                            /* invoiceId */ reference.split("-")[1], // extraído del formato
                                                                     // MYBILL-<invoiceId>-<payerId>
                            /* payerId */ reference.split("-")[2], // extraído del formato
                                                                   // MYBILL-<invoiceId>-<payerId>
                            /* amount */ BigDecimal.valueOf(t.amountInCents()).movePointLeft(2),
                            /* currency */ CurrencyType.valueOf(t.currency().toUpperCase()),
                            /* provider */ PaymentProvider.WOMPI,
                            /* reference */ reference);
                    return paymentRepository.save(created);
                });

        // 3) Actualizamos estado y metadatos
        tx.setExternalTransactionId(externalId);
        tx.setStatus(mapWompiStatus(t.status()));
        tx.setResponseCode(t.paymentMethodType()); // puedes mapear mejor si defines códigos concretos
        tx.setRejectionCause(t.redirectUrl()); // placeholder: ajusta con el campo correcto si Wompi entrega cause
        tx.setUpdatedAt(ZonedDateTime.now());

        paymentRepository.save(tx);
        log.info("Updated transaction {} to {}", reference, tx.getStatus());
    }

    @Override
    public PayuPaymentResponse createPayuPayment(PayuPaymentRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPayuPayment'");
    }

    private static TransactionStatus mapWompiStatus(String wompiStatus) {
        if (wompiStatus == null)
            return TransactionStatus.ERROR;
        return switch (wompiStatus.toUpperCase()) {
            case "APPROVED" -> TransactionStatus.APPROVED;
            case "DECLINED" -> TransactionStatus.DECLINED;
            case "VOIDED" -> TransactionStatus.VOIDED;
            case "PENDING" -> TransactionStatus.PENDING;
            default -> TransactionStatus.ERROR;
        };
    }

}
