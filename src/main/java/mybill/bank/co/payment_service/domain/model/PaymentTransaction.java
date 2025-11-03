package mybill.bank.co.payment_service.domain.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;
import mybill.bank.co.payment_service.domain.enumerations.TransactionStatus;
import mybill.bank.co.payment_service.domain.enumerations.PaymentProvider;

@Getter
@Setter
@AllArgsConstructor
public class PaymentTransaction {

    /** ID único de la transacción (generado por la pasarela). */
    private String transactionId;

    /**
     * Referencia única asociada al pago (puede ser el ID de la factura u otro
     * identificador).
     */
    private String paymentReference;

    /** Resultado general de la transacción: aprobado, rechazado, pendiente, etc. */
    private TransactionStatus status;

    /** Fecha y hora en que se realizó la transacción. */
    private ZonedDateTime transactionDate;

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

    /** Código de respuesta específico retornado por la pasarela. */
    private String responseCode;

    /** Causa o motivo del rechazo, si aplica. */
    private String rejectionCause;
}
