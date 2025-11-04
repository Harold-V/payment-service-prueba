package mybill.bank.co.payment_service.application.ports.in;

import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiWebHookResponse;

public interface PaymentUseCase {
    // 1) Crea intención de pago con Wompi (registra PENDING y retorna datos para
    // redirección)
    WompiPaymentResponse createWompiPayment(WompiPaymentRequest request);

    // 2) Procesa webhook de Wompi (actualiza estado y campos retornados)
    void handleWompiWebhook(WompiWebHookResponse response);

    // 3) Crea pago con PayU (similar idea; aquí puedes iniciar PSE/Tarjeta)
    PayuPaymentResponse createPayuPayment(PayuPaymentRequest request);
}
