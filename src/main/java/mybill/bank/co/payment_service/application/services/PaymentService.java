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
        var t = webhook.data().transaction();
        String reference = t.reference();
        String externalId = t.id();

        PaymentTransaction tx = paymentRepository.findByPaymentReference(reference)
                .orElseGet(() -> {
                    // Si llegó primero el webhook, crea registro mínimo
                    PaymentTransaction created = newPending(
                            /* invoiceId */ parseInvoiceId(reference),
                            /* payerId */ parsePayerId(reference),
                            /* amount */ BigDecimal.valueOf(t.amountInCents()).movePointLeft(2),
                            /* currency */ CurrencyType.valueOf(t.currency().toUpperCase()),
                            /* provider */ PaymentProvider.WOMPI,
                            /* reference */ reference);
                    return paymentRepository.save(created);
                });

        tx.setExternalTransactionId(externalId);
        tx.setStatus(mapWompiStatus(t.status()));
        // Usa campos reales del evento:
        tx.setResponseCode(t.paymentMethodType()); // o deja null si no te aporta
        // Wompi no envía "rejection_cause" aquí; evita poner redirectUrl como causa
        tx.setRejectionCause(null);

        tx.setUpdatedAt(ZonedDateTime.now());
        paymentRepository.save(tx);
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

    private String parseInvoiceId(String ref) {
        String[] parts = ref.split("-");
        return parts.length >= 3 ? parts[1] : "UNKNOWN";
    }

    private String parsePayerId(String ref) {
        String[] parts = ref.split("-");
        return parts.length >= 3 ? parts[2] : "UNKNOWN";
    }

}
