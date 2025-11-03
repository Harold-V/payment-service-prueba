package mybill.bank.co.payment_service.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybill.bank.co.payment_service.application.ports.out.PaymentTransactionWompiUseCase;
import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.config.WompiConfig;
import mybill.bank.co.payment_service.infrastructure.persistence.entities.PaymentTransactionEntity;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
@RequiredArgsConstructor
@Slf4j
public class WompiGatewayImpl implements PaymentTransactionWompiUseCase {

    private final WompiConfig wompiConfig;

    @Override
    public WompiPaymentResponse processPayment(WompiPaymentRequest request) {
        String reference = generatePaymentReference(request.invoiceId());

        // Convertir el monto a centavos
        long amountInCents = request.amountInCents()
                .multiply(BigDecimal.valueOf(100))
                .longValue();

        // Generar firma de integridad
        String integritySignature = generateIntegritySignature(
                reference,
                amountInCents,
                request.currency(),
                request.expirationTime());
    }

    @Override
    public WompiPaymentResponse getPaymentStatus(String paymentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentStatus'");
    }

    @Override
    public PaymentTransactionEntity processWebhookEvent(WompiPaymentResponse response) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processWebhookEvent'");
    }

    private String generatePaymentReference(String invoiceId) {

        return "MYBILLW-" + invoiceId + "-" + System.currentTimeMillis();
    }

    private String generateIntegritySignature(String reference, long amountInCents, CurrencyType currency,
            ZonedDateTime expirationTime) {
        try {
            StringBuilder concatenatedText = new StringBuilder();
            concatenatedText.append(reference)
                    .append(amountInCents)
                    .append(currency);

            // Si hay fecha de expiración, incluirla
            if (expirationTime != null) {
                concatenatedText.append(expirationTime.format(DateTimeFormatter.ISO_INSTANT));
            }

            concatenatedText.append(wompiConfig.getPrivateKey());

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
