package mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WompiPaymentResponse(
                String event,
                Data data,
                @JsonProperty("sent_at") String sentAt) {

        public record Data(
                        Transaction transaction) {

        }

        public record Transaction(
                        String id,
                        @JsonProperty("amount_in_cents") long amountInCents,
                        String reference,
                        @JsonProperty("customer_email") String customerEmail,
                        String currency,
                        @JsonProperty("payment_method_type") String paymentMethodType,
                        @JsonProperty("redirect_url") String redirectUrl,
                        String status,
                        @JsonProperty("shipping_address") String shippingAddress,
                        @JsonProperty("payment_link_id") String paymentLinkId,
                        @JsonProperty("payment_source_id") String paymentSourceId) {
        }

}
