package mybill.bank.co.payment_service.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybill.bank.co.payment_service.application.ports.out.TransactionWompiUseCase;
import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.config.WompiConfig;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WompiGatewayImpl implements TransactionWompiUseCase {

    private final WompiConfig wompiConfig;

    @Override
    public WompiPaymentResponse processPayment(WompiPaymentRequest request) {

        log.info("Initializing Wompi payment for invoice: {}", request.invoiceId());

        // Calcular tiempo de expiración (60 minutos desde ahora en UTC)
        String expirationTime = Instant.now()
                .plus(60, ChronoUnit.MINUTES)
                .toString();

        String reference = generatePaymentReference(request.invoiceId(), request.payerId());

        // Generar firma de integridad
        String integritySignature = generateIntegritySignature(
                reference,
                request.amountInCents(),
                request.currency(),
                expirationTime);

        WompiPaymentResponse.Signature signature = new WompiPaymentResponse.Signature(integritySignature);

        return new WompiPaymentResponse(
                request.currency(),
                request.amountInCents(),
                reference,
                wompiConfig.getPublicKey(),
                signature,
                wompiConfig.getRedirectUrl(),
                expirationTime,
                request.customerData(),
                request.shippingAddress());

    }

    private String generatePaymentReference(String invoiceId, String payerId) {
        return "MYBILL-" + invoiceId + "-" + payerId; // Preguntar si es correcto este formato
    }

    private String generateIntegritySignature(String reference, long amountInCents, CurrencyType currency,
            String expirationTime) {
        try {
            StringBuilder concatenatedText = new StringBuilder();
            concatenatedText.append(reference)
                    .append(amountInCents)
                    .append(currency);

            // Si hay fecha de expiración, incluirla
            if (expirationTime != null) {
                concatenatedText.append(expirationTime);
            }

            concatenatedText.append(wompiConfig.getIntegritySecret());

            log.info("Concatenated text for integrity signature: {}", concatenatedText.toString());

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
