package mybill.bank.co.payment_service.infrastructure.adapter.dto.payu;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PayuPaymentResponse(
                String code,
                String error,
                TransactionResponse transactionResponse,
                String referenceCode,
                String payerId,
                String invoiceId) {

        public record TransactionResponse(
                        Long orderId,
                        String transactionId,
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
                        String referenceQuestionnaire,
                        ExtraParameters extraParameters,
                        AdditionalInfo additionalInfo) {
        }

        public record ExtraParameters(
                        @JsonProperty("TRANSACTION_CYCLE") String transactionCycle,
                        @JsonProperty("BANK_URL") String bankUrl,
                        @JsonProperty("BANK_REFERENCED_CODE") String bankReferencedCode,
                        @JsonProperty("TERMINAL_ID") String terminalId) {
        }

        public record AdditionalInfo(
                        String paymentNetwork,
                        String rejectionType,
                        String responseNetworkMessage,
                        String travelAgencyAuthorizationCode,
                        String cardType,
                        String transactionType) {
        }
}
