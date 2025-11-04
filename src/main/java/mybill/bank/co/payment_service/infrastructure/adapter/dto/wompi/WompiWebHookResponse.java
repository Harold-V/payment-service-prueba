package mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WompiWebHookResponse(
                String event,
                Data data,
                String environment,
                Long timestamp,
                @JsonProperty("sent_at") String sentAt) {

        public record Data(
                        Transaction transaction) {
        }

        public record Transaction(
                        String id,
                        @JsonProperty("amount_in_cents") Long amountInCents,
                        String reference,
                        @JsonProperty("payment_method_type") String paymentMethodType,
                        String status,
                        @JsonProperty("payment_method") PaymentMethod paymentMethod) {
        }

        public record PaymentMethod(
                        @JsonProperty("extra") Extra extra) {
        }

        public record Extra(
                        @JsonProperty("return_code") String returnCode) {
        }

}