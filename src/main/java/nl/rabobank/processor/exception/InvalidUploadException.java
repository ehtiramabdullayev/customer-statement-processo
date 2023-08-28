package nl.rabobank.processor.exception;

import org.springframework.http.HttpStatus;

public class InvalidUploadException extends RuntimeException implements CustomException {
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public InvalidUploadException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public InvalidUploadException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
