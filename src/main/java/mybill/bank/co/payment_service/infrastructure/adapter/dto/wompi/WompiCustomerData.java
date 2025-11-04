package mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para los datos del cliente
 */

public record WompiCustomerData(
        @NotBlank String email,
        @NotBlank String phoneNumber,
        @NotBlank String phoneNumberPrefix,
        @NotBlank String legalId,
        String legalIdType) {
}
