package mybill.bank.co.payment_service.domain.enumerations;

/**
 * Representa los métodos de pago disponibles para el usuario.
 */
public enum PaymentType {
    /**
     * Representa un método de pago con tarjeta de crédito.
     */
    CREDIT,
    /**
     * Representa un método de pago con tarjeta de débito.
     */
    DEBIT,
    /**
     * Representa un método de pago via PSE (Pagos Seguros en Línea).
     */
    PSE,
    UNKNOWN,
    CASH
}
