package mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi;

import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;

public record WompiPaymentResponse(

                CurrencyType currency,
                Long amountInCents,
                String reference,
                String publicKey,
                Signature signature,
                String redirectUrl,
                String expirationTime,
                WompiCustomerData customerData,
                WompiShippingAddress shippingAddress) {

        public record Signature(
                        String integrity) {
        }

}
