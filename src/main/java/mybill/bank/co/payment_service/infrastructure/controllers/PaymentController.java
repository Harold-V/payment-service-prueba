package mybill.bank.co.payment_service.infrastructure.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybill.bank.co.payment_service.application.ports.in.PaymentUseCase;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiWebHookResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment/wompi")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentUseCase paymentUseCase;

    @PostMapping
    public ResponseEntity<WompiPaymentResponse> create(@RequestBody WompiPaymentRequest request) {
        WompiPaymentResponse resp = paymentUseCase.createWompiPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestHeader Map<String, String> headers,
            @RequestBody WompiWebHookResponse body,
            HttpServletRequest servletRequest) {
        // (Opcional) valida firma/checksum aquí con tu WompiConfig antes de delegar
        paymentUseCase.handleWompiWebhook(body);
        return ResponseEntity.ok().build();
    }

}
