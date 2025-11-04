package mybill.bank.co.payment_service.infrastructure.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mybill.bank.co.payment_service.application.ports.out.TransactionWompiUseCase;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentRequest;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi.WompiPaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment/wompi")
@RequiredArgsConstructor
@Slf4j
public class WompiPaymentController {
    private final TransactionWompiUseCase wompiUseCase;

    @PostMapping
    public ResponseEntity<WompiPaymentResponse> create(@RequestBody WompiPaymentRequest request) {
        WompiPaymentResponse resp = wompiUseCase.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestHeader Map<String, String> headers,
            @RequestBody Map<String, Object> body,
            HttpServletRequest servletRequest) {
        return ResponseEntity.ok().build();
    }

}
