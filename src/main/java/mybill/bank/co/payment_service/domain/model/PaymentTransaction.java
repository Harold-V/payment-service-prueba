package mybill.bank.co.payment_service.domain.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;
import mybill.bank.co.payment_service.domain.enumerations.TransactionStatus;
import mybill.bank.co.payment_service.domain.enumerations.PaymentProvider;
import mybill.bank.co.payment_service.domain.enumerations.PaymentType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransaction {

    /** ID único de la transacción */
    private UUID transactionId;

    /** ID único de la transacción (generado por la pasarela). */
    private String externalTransactionId;

    /**
     * Referencia única asociada al pago (puede ser el ID de la factura u otro
     * identificador).
     */
    private String paymentReference;

    /** Resultado general de la transacción: aprobado, rechazado, pendiente, etc. */
    private TransactionStatus status;

    /** Identificador del pagador (usuario, cliente o entidad). */
    private String payerId;

    /** ID de la factura en el sistema MyBill asociada al pago. */
    private String invoiceId;

    /** Monto total del pago. */
    private BigDecimal amount;

    /** Monto total del pago. */
    private CurrencyType currencyType;

    /** Nombre de la pasarela de pago utilizada (Wompi, PayU, etc.). */
    private PaymentProvider paymentProvider;

    /** Tipo de pago (PSE, tarjeta, etc.). */
    private PaymentType paymentType;

    /** Código de respuesta específico retornado por la pasarela. */
    private String responseCode;

    /** Causa o motivo del rechazo, si aplica. */
    private String rejectionCause;

    /** Fecha y hora de creación de la transacción. */
    private ZonedDateTime createdAt;

    /** Fecha y hora de última actualización de la transacción. */
    private ZonedDateTime updatedAt;

    /**
     * Crea una nueva transacción de pago en estado PENDING.
     * 
     * @param invoiceId ID de la factura asociada
     * @param payerId   Identificador del pagador
     * @param amount    Monto del pago
     * @param provider  Proveedor de pago
     * @param reference Referencia única del pago
     * @return Nueva instancia de PaymentTransaction en estado pendiente
     */
    public static PaymentTransaction createPending(
            String invoiceId,
            String payerId,
            BigDecimal amount,
            PaymentProvider provider,
            String reference) {

        ZonedDateTime now = ZonedDateTime.now();

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.transactionId = UUID.randomUUID();
        transaction.externalTransactionId = null;
        transaction.paymentReference = reference;
        transaction.status = TransactionStatus.PENDING;
        transaction.payerId = payerId;
        transaction.invoiceId = invoiceId;
        transaction.amount = amount;
        transaction.currencyType = CurrencyType.COP;
        transaction.paymentProvider = provider;
        transaction.paymentType = null;
        transaction.responseCode = null;
        transaction.rejectionCause = null;
        transaction.createdAt = now;
        transaction.updatedAt = now;

        return transaction;
    }

}
