package mybill.bank.co.payment_service.application.services;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybill.bank.co.payment_service.application.ports.in.PaymentUseCase;
import mybill.bank.co.payment_service.application.ports.out.TransactionWompiUseCase;
import mybill.bank.co.payment_service.domain.enumerations.PaymentProvider;
import mybill.bank.co.payment_service.domain.enumerations.PaymentType;
import mybill.bank.co.payment_service.domain.enumerations.TransactionStatus;
import mybill.bank.co.payment_service.domain.model.PaymentTransaction;
import mybill.bank.co.payment_service.domain.ports.PaymentTransactionRepositoryPort;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiWebHookResponse;
import mybill.bank.co.payment_service.infrastructure.config.WompiConfig;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements PaymentUseCase {

    private final TransactionWompiUseCase wompiGateway;
    private final WompiConfig wompiConfig;
    private final PaymentTransactionRepositoryPort paymentRepository;

    @Override
    @Transactional
    public WompiPaymentResponse createWompiPayment(WompiPaymentRequest request) {
        // 1) Llama a la pasarela para construir los datos de pago (ref, firma, etc.)
        WompiPaymentResponse response = wompiGateway.processPayment(request);

        // 2) Persistir una transacción PENDING (si ya existe por reference, evita
        // duplicar)
        String reference = response.reference();
        paymentRepository.findByPaymentReference(reference).ifPresentOrElse(
                existing -> {
                    log.info("Transaction with reference {} already exists. Skipping create.", reference);
                },
                () -> {
                    // amountInCents -> BigDecimal en unidades
                    BigDecimal amount = BigDecimal.valueOf(response.amountInCents()).movePointLeft(2);

                    PaymentTransaction pending = PaymentTransaction.createPending(
                            request.invoiceId(),
                            request.payerId(),
                            amount,
                            PaymentProvider.WOMPI,
                            reference);

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
                    BigDecimal amount = BigDecimal.valueOf(t.amountInCents()).movePointLeft(2);

                    PaymentTransaction created = PaymentTransaction.createPending(
                            parseInvoiceId(reference),
                            parsePayerId(reference),
                            amount,
                            PaymentProvider.WOMPI,
                            reference);

                    return paymentRepository.save(created);
                });

        // Actualizar con datos del webhook
        tx.setExternalTransactionId(externalId);
        tx.setStatus(mapWompiStatus(t.status()));
        tx.setPaymentType(mapWompiPaymentType(t.paymentMethodType()));
        tx.setResponseCode(t.paymentMethod().extra().returnCode());
        tx.setRejectionCause(null);
        tx.setUpdatedAt(ZonedDateTime.now());

        paymentRepository.save(tx);

        log.info("Updated transaction {} from webhook. Status: {}, ExternalId: {}",
                reference, tx.getStatus(), externalId);
    }

    @Override
    public boolean verifyWompiEventIntegrity(String checksum, WompiWebHookResponse payload) {
        String generatedSignature = generateEventSignature(payload);
        boolean isValid = generatedSignature.equalsIgnoreCase(checksum);
        if (isValid) {
            log.info("Wompi webhook integrity verified successfully.");
        } else {
            log.warn("Wompi webhook integrity verification failed. Expected: {}, Generated: {}", checksum,
                    generatedSignature);
        }
        return isValid;
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

    private static PaymentType mapWompiPaymentType(String wompiType) {
        if (wompiType == null)
            return null;
        return switch (wompiType.toUpperCase()) {
            case "CARD" -> PaymentType.CREDIT;
            case "PSE" -> PaymentType.PSE;
            default -> null;
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

    private String generateEventSignature(WompiWebHookResponse webhook) {
        try {
            StringBuilder concatenatedText = new StringBuilder();

            String id = webhook.data().transaction().id();
            Long amountInCents = webhook.data().transaction().amountInCents();
            String status = webhook.data().transaction().status();
            String timestamp = webhook.timestamp().toString();
            concatenatedText.append(id)
                    .append(status)
                    .append(amountInCents)
                    .append(timestamp)
                    .append(wompiConfig.getEventsKey());

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(concatenatedText.toString().getBytes(StandardCharsets.UTF_8));

            // Convertir a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception e) {
            log.error("Error generating integrity signature", e);
            throw new RuntimeException("Failed to generate integrity signature", e);
        }

    }

}
