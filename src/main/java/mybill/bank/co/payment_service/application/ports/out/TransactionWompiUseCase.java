package mybill.bank.co.payment_service.application.ports.out;

import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiWebHookResponse;
import mybill.bank.co.payment_service.domain.model.PaymentTransaction;

public interface TransactionWompiUseCase {

    WompiPaymentResponse processPayment(WompiPaymentRequest request);

    PaymentTransaction processWebhookEvent(WompiWebHookResponse response);

}