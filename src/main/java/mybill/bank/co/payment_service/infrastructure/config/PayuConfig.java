package mybill.bank.co.payment_service.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuración de PayU que contiene los detalles necesarios para interactuar
 * con la pasarela de pagos.
 * Esta clase es utilizada para cargar las propiedades relacionadas con PayU
 * desde el archivo de configuración
 * y proveerlas a los componentes de la aplicación que necesiten interactuar con
 * la API de PayU.
 * 
 * <p>
 * Las propiedades configuradas son: cuenta, comercio, API, y transacción. Cada
 * una de estas categorías
 * está representada por una clase interna que almacena la configuración
 * respectiva.
 * </p>
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "payu")
public class PayuConfig {

    /**
     * Información de la cuenta de PayU.
     * Esta cuenta está asociada con la pasarela de pagos y contiene el
     * identificador único de la cuenta.
     */
    private Account account;

    /**
     * Información del comercio registrado en PayU.
     * Esta propiedad incluye el identificador único del comercio utilizado para las
     * transacciones.
     */
    private Merchant merchant;

    /**
     * Información de la API de PayU.
     * Contiene el login, la clave API y la URL para acceder a la API de PayU.
     */
    private Api api;

    /**
     * Información de la transacción.
     * Incluye detalles como el idioma, el país de pago y un indicador de si se está
     * realizando una transacción de prueba.
     */
    private Transaction transaction;

    /**
     * Información de URLs de notificación.
     */
    private Notification notification;

    /**
     * Clase que representa la cuenta de PayU.
     * Contiene el identificador único de la cuenta utilizado para las
     * transacciones.
     */
    @Getter
    @Setter
    public static class Account {
        /**
         * Identificador único de la cuenta en PayU.
         * Este identificador es utilizado para asociar transacciones con la cuenta
         * correcta.
         */
        private String id;
    }

    /**
     * Clase que representa el comercio en PayU.
     * Contiene el identificador único del comercio registrado en la plataforma.
     */
    @Getter
    @Setter
    public static class Merchant {
        /**
         * Identificador único del comercio registrado en PayU.
         * Este identificador es utilizado para autenticar y procesar pagos en nombre
         * del comercio.
         */
        private String id;
    }

    /**
     * Clase que contiene la configuración de la API de PayU.
     * Incluye el login, la clave API y la URL base de la API de PayU.
     */
    @Getter
    @Setter
    public static class Api {
        /**
         * Login utilizado para autenticar la conexión con la API de PayU.
         */
        private String login;

        /**
         * Clave de acceso para la API de PayU.
         * Esta clave es necesaria para realizar transacciones a través de la pasarela
         * de pagos.
         */
        private String key;

        /**
         * URL base de la API de PayU.
         * Esta URL se utiliza para realizar las solicitudes a la API de PayU.
         */
        private String url;
    }

    /**
     * Clase que representa la configuración de la transacción.
     * Contiene información sobre el idioma, el país de pago y si se trata de una
     * transacción de prueba.
     */
    @Getter
    @Setter
    public static class Transaction {
        /**
         * Idioma en el que se debe realizar la transacción.
         * Este campo puede tener valores como "es" para español, "en" para inglés,
         * entre otros.
         */
        private String language;

        /**
         * País en el que se realiza el pago.
         * Este campo es utilizado para determinar el país de la transacción y las
         * reglas asociadas.
         */
        private String paymentCountry;

        /**
         * Indica si la transacción es una transacción de prueba.
         * Si es {@code true}, se realizará una transacción en modo de prueba sin
         * realizar un cargo real.
         */
        private boolean test;
    }

    /**
     * Clase que representa la configuración de URLs de notificación.
     * Contiene información sobre las URL de respuesta y redirección de backend y
     * frontend.
     * 
     * Docs en: https://developers.payu.com/europe/docs/payment-flows/lifecycle/
     */
    @Getter
    @Setter
    public static class Notification {
        /**
         * URL de confirmación de la orden en el backend
         */
        private String notifyUrl;

        /**
         * Corresponde al campo RESPONSE_URL de PSE. Es la URL del frontend a la cual se
         * redirige
         * después de la respuesta de PSE
         */
        private String responseUrl;
    }
}
