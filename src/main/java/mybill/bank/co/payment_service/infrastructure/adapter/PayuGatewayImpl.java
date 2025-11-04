package mybill.bank.co.payment_service.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybill.bank.co.payment_service.application.ports.out.TransactionPayuUseCase;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.payu.PayuQueryResponse;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayuGatewayImpl implements TransactionPayuUseCase {

    @Override
    public PayuPaymentResponse processPayment(PayuPaymentRequest paymentRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processPayment'");
    }

    @Override
    public PayuQueryResponse getPaymentStatus(String paymentId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPaymentStatus'");
    }

}
