package mybill.bank.co.payment_service.infrastructure.adapter.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para los datos del cliente
 */

public record CustomerData(
                @NotBlank String email,
                @NotBlank String phoneNumber,
                @NotBlank String phoneNumberPrefix,
                @NotBlank String legalId,
                String legalIdType) {
}
