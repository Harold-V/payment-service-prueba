package mybill.bank.co.payment_service.infrastructure.adapter.dto.payu;

/**
 * Representa los detalles de la tarjeta de crédito o débito para el
 * procesamiento de pagos.
 * 
 * <p>
 * Incluye los campos necesarios para procesar pagos mediante tarjeta, tales
 * como el número de tarjeta,
 * el código de seguridad (CVV), la fecha de expiración y el nombre del titular
 * de la tarjeta.
 * </p>
 * 
 */
public record CardDetails(
        /**
         * Número de la tarjeta de crédito o débito utilizado para procesar el pago.
         * Este campo es obligatorio para realizar pagos mediante tarjeta.
         */
        String creditCardNumber,

        /**
         * Código de seguridad (CVV) de la tarjeta de crédito o débito.
         * Este campo es necesario para validar la transacción y debe ser un número de 3
         * o 4 dígitos.
         */
        String securityCode,

        /**
         * Fecha de expiración de la tarjeta en formato MM/AA.
         * Este campo es necesario para verificar la validez de la tarjeta durante la
         * transacción.
         */
        String expirationDate,

        /**
         * Nombre completo del titular de la tarjeta.
         * Este campo es utilizado para confirmar la identidad del titular de la tarjeta
         * durante el pago.
         */
        String cardHolderName,

        /**
         * Nombre del proveedor de la tarjeta (e.g. VISA, MasterCard).
         */
        String cardProvider) {

}
