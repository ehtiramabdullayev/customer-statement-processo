package nl.rabobank.processor.exception;

import org.springframework.http.HttpStatus;
public class UploadTypeNotSupported extends RuntimeException implements CustomException {
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public UploadTypeNotSupported(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public UploadTypeNotSupported(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
