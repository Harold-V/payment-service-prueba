package mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WompiWebHookResponse(
                String event,
                Data data,
                String environment,
                Signature signature,
                Long timestamp,
                @JsonProperty("sent_at") String sentAt) {

        public record Data(
                        Transaction transaction) {
        }

        public record Transaction(
                        String id,
                        @JsonProperty("amount_in_cents") Long amountInCents,
                        String reference,
                        @JsonProperty("customer_email") String customerEmail,
                        String currency,
                        @JsonProperty("payment_method_type") String paymentMethodType,
                        @JsonProperty("redirect_url") String redirectUrl,
                        String status,
                        @JsonProperty("shipping_address") WompiWebhookShippingAddress shippingAddress,
                        @JsonProperty("payment_link_id") String paymentLinkId,
                        @JsonProperty("payment_source_id") String paymentSourceId) {
        }

        public record Signature(
                        List<String> properties,
                        String checksum) {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record WompiWebhookShippingAddress(
                        @JsonProperty("address_line_1") String addressLine1,
                        @JsonProperty("address_line_2") String addressLine2,
                        @JsonProperty("city") String city,
                        @JsonProperty("phone_number") String phoneNumber,
                        @JsonProperty("region") String region,
                        @JsonProperty("country") String country) {
        }

}