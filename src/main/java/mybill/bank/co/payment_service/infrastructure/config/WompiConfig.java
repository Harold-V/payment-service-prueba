package mybill.bank.co.payment_service.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuración de Wompi que contiene los detalles necesarios para interactuar
 * con la pasarela de pagos.
 * Esta clase es utilizada para cargar las propiedades relacionadas con Wompi
 * desde el archivo de configuración
 * y proveerlas a los componentes de la aplicación que necesiten interactuar con
 * la API de Wompi.
 * 
 * <p>
 * Las propiedades configuradas incluyen las claves de autenticación (pública,
 * privada, eventos) y el secreto de integridad utilizado para verificar
 * la autenticidad de las notificaciones de webhook.
 * </p>
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "wompi")
public class WompiConfig {

    /**
     * Clave privada de Wompi.
     * Esta clave se utiliza para realizar operaciones en el servidor que requieren
     * autenticación privilegiada, como crear transacciones o consultar información
     * sensible.
     */
    private String privateKey;

    /**
     * Clave pública de Wompi.
     * Esta clave se utiliza típicamente en el frontend para inicializar widgets
     * de pago y realizar operaciones que no requieren privilegios elevados.
     */
    private String publicKey;

    /**
     * Clave de eventos de Wompi.
     * Esta clave se utiliza para autenticar y gestionar los eventos de webhook
     * enviados por Wompi cuando ocurren cambios en el estado de las transacciones.
     */
    private String eventsKey;

    /**
     * Secreto de integridad de Wompi.
     * Este secreto se utiliza para verificar la firma de los eventos recibidos
     * vía webhook, garantizando que provienen efectivamente de Wompi y no han
     * sido manipulados.
     */
    private String integritySecret;

    /**
     * URL del webhook configurada en Wompi.
     * Esta URL es donde Wompi enviará las notificaciones de eventos relacionados
     * con las transacciones.
     */
    private String webhookUrl;

    /**
     * URL de redirección después del pago.
     * Esta URL es donde se redirigirá al usuario después de completar el proceso
     * de pago en Wompi.
     */
    private String redirectUrl;
}