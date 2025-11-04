package mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi;

import java.util.UUID;

import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.CustomerData;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.ShippingAddress;

public record WompiPaymentRequest(
                String invoiceId,
                String payerId,
                long amountInCents,
                CurrencyType currency,
                CustomerData customerData,
                ShippingAddress shippingAddress) {

        public WompiPaymentRequest {
                /*
                 * Traemos desde el microservicio de Invoice la información de la factura y del
                 * pagador (cliente) Por el momento lo simulamos
                 */
                invoiceId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                payerId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                amountInCents = 10000000L; // Ejemplo: 100,000.00 COP = 10,000,000 centavos
                currency = CurrencyType.COP;
        }
}
