package mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import jakarta.validation.constraints.NotBlank;
import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.CustomerData;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.ShippingAddress;

public record WompiPaymentRequest(
        @NotBlank(message = "Invoice ID is required") String invoiceId,

        @NotBlank(message = "Payer ID is required") String payerId,

        CurrencyType currency,
        BigDecimal amountInCents,
        CustomerData customerData,
        ShippingAddress shippingAddress,
        ZonedDateTime expirationTime) {

    public WompiPaymentRequest {
        if (currency == null) {
            currency = CurrencyType.COP;
        }

    }

}
