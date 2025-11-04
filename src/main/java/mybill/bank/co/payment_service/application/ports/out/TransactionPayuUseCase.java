package mybill.bank.co.payment_service.application.ports.out;

import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuQueryResponse;

public interface TransactionPayuUseCase {

    PayuPaymentResponse processPayment(PayuPaymentRequest paymentRequest);

    PayuQueryResponse getPaymentStatus(String paymentId);
}
