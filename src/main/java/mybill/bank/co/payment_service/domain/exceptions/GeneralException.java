package mybill.bank.co.payment_service.domain.exceptions;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

/**
 * General application exception. All custom exceptions
 * must inherit from this class
 */
public class GeneralException extends RuntimeException {

    public GeneralException(String message) {
        super(message);
    }

    /**
     * Gets the HTTP status associated with the exception. By default it returns
     * BAD_REQUEST, but can be overridden by concrete implementations.
     *
     * @return the HTTP status associated with the exception
     */
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    /**
     * Gets the appropriate logging level for the exception
     *
     * @return the logging level
     * @see {@link Level}
     */
    public Level getLogLevel() {
        return Level.INFO;
    }

}