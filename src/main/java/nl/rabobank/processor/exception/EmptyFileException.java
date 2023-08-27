package nl.rabobank.processor.exception;


import org.springframework.http.HttpStatus;

public class EmptyFileException extends RuntimeException implements CustomException{
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public EmptyFileException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public EmptyFileException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
