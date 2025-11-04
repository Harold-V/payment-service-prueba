package mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi;

import mybill.bank.co.payment_service.infrastructure.adapter.dto.CustomerData;
import mybill.bank.co.payment_service.infrastructure.adapter.dto.ShippingAddress;

public record WompiPaymentRequest(

        CustomerData customerData,
        ShippingAddress shippingAddress) {

}
