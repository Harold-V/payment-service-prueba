package mybill.bank.co.payment_service.infrastructure.adapter.dto.payu;

import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentResponse.ExtraParameters;

public record PayuQueryResponse(String code,
                String error,
                Result result) {

        public record Result(Payload payload) {
        }

        public record Payload(
                        String state,
                        String paymentNetworkResponseCode,
                        String paymentNetworkResponseErrorMessage,
                        String trazabilityCode,
                        String authorizationCode,
                        String pendingReason,
                        String responseCode,
                        String errorCode,
                        String responseMessage,
                        String transactionDate,
                        String transactionTime,
                        Long operationDate,
                        ExtraParameters extraParameters) {
        }
}