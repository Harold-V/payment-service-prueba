package mybill.bank.co.payment_service.infrastructure.adapter.dto.wompi;

import jakarta.validation.constraints.NotBlank;

/**
 * Representa la dirección de envío asociada con una transacción de pago.
 * Esta clase se utiliza para almacenar la información de la dirección de envío
 * 
 * <p>
 * Contiene todos los campos necesarios para describir la dirección de envío,
 * tales como la calle, ciudad, estado, país y código postal.
 * </p>
 */
public record WompiShippingAddress(
                /**
                 * Calle principal de la dirección de envío.
                 * Este campo debe contener la primera línea de la dirección de envío.
                 */
                @NotBlank(message = "La dirección (línea 1) es obligatoria") String addressLine1,

                /**
                 * Segunda línea de la dirección de envío, si es aplicable.
                 * Este campo es opcional y se utiliza para agregar detalles adicionales a la
                 * dirección, como un apartamento o edificio.
                 */
                String addressLine2,

                /**
                 * Ciudad donde se encuentra la dirección de envío.
                 * Este campo es obligatorio y debe contener el nombre de la ciudad.
                 */
                @NotBlank(message = "La ciudad es obligatoria") String city,

                /* Numero de telefono */
                @NotBlank(message = "El número de teléfono es obligatorio") String phoneNumber,

                /**
                 * Estado o provincia de la dirección de envío.
                 * Este campo es obligatorio
                 */
                @NotBlank(message = "La región es obligatoria") String region,

                /* País en formato "CO" */
                @NotBlank(message = "El país es obligatorio") String country) {

        public WompiShippingAddress {
                if (addressLine2 == null) {
                        addressLine2 = "";
                }
        }

}
