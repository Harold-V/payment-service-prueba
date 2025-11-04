package mybill.bank.co.payment_service.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import mybill.bank.co.payment_service.infrastructure.config.WompiConfig;

@Component
@RequiredArgsConstructor
public class WompiEventVerifier {

    private final WompiConfig wompiConfig;
    private final ObjectMapper objectMapper;

    /**
     * Valida el checksum del evento de Wompi.
     * 
     * @param headerChecksum valor del header "X-Event-Checksum"
     * @param payload        mapa crudo del body JSON
     */

    public boolean isValid(String headerChecksum, Map<String, Object> payload) {
        if (headerChecksum == null || headerChecksum.isBlank())
            return false;

        // Convertimos a árbol para navegar properties dinamicamente
        JsonNode root = objectMapper.convertValue(payload, JsonNode.class);

        // signature.properties: lista de rutas tipo "transaction.id"
        JsonNode signature = root.path("signature");
        JsonNode propertiesNode = signature.path("properties");
        if (!propertiesNode.isArray())
            return false;

        StringBuilder sb = new StringBuilder();

        // Siempre empezar desde data.*
        JsonNode dataNode = root.path("data");
        for (JsonNode prop : propertiesNode) {
            String[] parts = prop.asText("").split("\\.");
            JsonNode cursor = dataNode;
            for (String p : parts) {
                cursor = cursor.path(p);
            }
            if (cursor.isMissingNode() || cursor.isNull())
                return false;
            // concatenar exactamente el valor (sin separadores)
            sb.append(cursor.asText());
        }

        // Concatenar timestamp
        JsonNode ts = root.path("timestamp");
        if (!ts.isNumber())
            return false;
        sb.append(ts.asText());

        // Concatenar secreto de eventos (NO es la llave privada ni el integrity secret)
        String secret = wompiConfig.getEventsKey();
        sb.append(secret);

        // SHA-256 en HEX
        String computed = sha256Hex(sb.toString());

        // Comparar normalizando a mayúsculas
        return computed.equalsIgnoreCase(headerChecksum);
    }

    private static String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1)
                    hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot compute SHA-256", e);
        }
    }
}
