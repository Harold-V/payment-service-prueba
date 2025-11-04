package mybill.bank.co.payment_service.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybill.bank.co.payment_service.application.ports.in.PaymentUseCase;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiWebHookResponse;
import mybill.bank.co.payment_service.infrastructure.security.WompiEventVerifier;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@RestController
@RequestMapping("/payment/wompi")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentUseCase paymentUseCase;
    private final WompiEventVerifier wompiEventVerifier;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<WompiPaymentResponse> create(@RequestBody WompiPaymentRequest request) {
        WompiPaymentResponse resp = paymentUseCase.createWompiPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(
            @RequestHeader(value = "X-Event-Checksum", required = false) String checksum,
            @RequestBody Map<String, Object> payload) {

        // 1) Validar firma
        if (!wompiEventVerifier.isValid(checksum, payload)) {
            // Si no validamos, devolvemos 400 (Wompi reintentará)
            return ResponseEntity.badRequest().build();
        }

        // 2) Convertir al DTO tipado para la capa de aplicación
        WompiWebHookResponse body = objectMapper.convertValue(payload, WompiWebHookResponse.class);

        // 3) Procesar (idempotente: tu servicio ya hace upsert/actualización por
        // reference)
        paymentUseCase.handleWompiWebhook(body);

        // 4) Responder 200 (Wompi no reintenta)
        return ResponseEntity.ok().build();
    }

}
