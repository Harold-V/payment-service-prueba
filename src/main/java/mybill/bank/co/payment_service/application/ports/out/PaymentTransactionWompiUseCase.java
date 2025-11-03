package mybill.bank.co.payment_service.application.ports.out;

import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.persistence.entities.PaymentTransactionEntity;

public interface PaymentTransactionWompiUseCase {

    WompiPaymentResponse processPayment(WompiPaymentRequest request);

    WompiPaymentResponse getPaymentStatus(String paymentId);

    PaymentTransactionEntity processWebhookEvent(WompiPaymentResponse response);

}