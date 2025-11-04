package mybill.bank.co.payment_service.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybill.bank.co.payment_service.application.ports.in.PaymentUseCase;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiWebHookResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/webhook", consumes = "application/json")
    public ResponseEntity<Void> webhook(
            @RequestHeader(value = "X-Event-Checksum", required = false) String checksum,
            @RequestBody WompiWebHookResponse payload) {

        if (paymentUseCase.verifyWompiEventIntegrity(checksum, payload)) {
            paymentUseCase.handleWompiWebhook(payload);
        } else {
            log.warn("Invalid webhook checksum received from Wompi.");
            log.info("Webhook payload: {}", payload.toString());
            log.info("Webhook checksum: {}", checksum);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
