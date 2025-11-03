package mybill.bank.co.payment_service.infrastructure.adapter.dto.payu;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;
import mybill.bank.co.payment_service.domain.enumerations.PaymentType;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.CustomerData;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.ShippingAddress;

public record PayuPaymentRequest(

                @NotBlank(message = "Invoice ID is required") String invoiceId,

                @NotBlank(message = "Payer ID is required") String payerId,

                BigDecimal amount,

                CurrencyType currency,

                PaymentType paymentMethod,

                String fullName,

                CustomerData customerData,

                ShippingAddress shippingAddress,

                UUID paymentMethodId,

                CardDetails cardDetails,

                String userType,

                String pseCode) {

        public PayuPaymentRequest {
                if (currency == null) {
                        currency = CurrencyType.COP;
                }

        }

}
