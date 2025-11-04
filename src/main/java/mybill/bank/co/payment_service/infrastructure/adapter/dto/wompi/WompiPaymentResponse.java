package mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi;

import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.CustomerData;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.ShippingAddress;

public record WompiPaymentResponse(

        CurrencyType currency,
        Long amountInCents,
        String reference,
        String publicKey,
        Signature signature,
        String redirectUrl,
        String expirationTime,
        CustomerData customerData,
        ShippingAddress shippingAddress) {

    public record Signature(
            String integrity) {
    }

}
