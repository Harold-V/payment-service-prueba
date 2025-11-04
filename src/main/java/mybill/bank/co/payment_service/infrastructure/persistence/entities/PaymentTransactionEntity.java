package mybill.bank.co.payment_service.infrastructure.persistence.entities;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mybill.bank.co.payment_service.domain.enumerations.CurrencyType;
import mybill.bank.co.payment_service.domain.enumerations.PaymentProvider;
import mybill.bank.co.payment_service.domain.enumerations.TransactionStatus;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "payment_transactions")
public class PaymentTransactionEntity {
    @Id
    @Column(name = "transaction_id", nullable = false, length = 100)
    private UUID transactionId;

    @Column(name = "external_transaction_id", length = 100)
    private String externalTransactionId;

    @Column(name = "payment_reference", nullable = false, length = 100, unique = true)
    private String paymentReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TransactionStatus status;

    @Column(name = "payer_id", nullable = false, length = 50)
    private String payerId;

    @Column(name = "invoice_id", nullable = false, length = 50)
    private String invoiceId;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_type", nullable = false, length = 10)
    private CurrencyType currencyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_provider", nullable = false, length = 30)
    private PaymentProvider paymentProvider;

    @Column(name = "response_code", length = 20)
    private String responseCode;

    @Column(name = "rejection_cause", length = 500)
    private String rejectionCause;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

}
