package mybill.bank.co.payment_service.application.ports.out;

import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;

public interface TransactionWompiUseCase {

    WompiPaymentResponse processPayment(WompiPaymentRequest request);

}