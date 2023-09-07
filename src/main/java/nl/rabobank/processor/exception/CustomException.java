package nl.rabobank.processor.exception;

import org.springframework.http.HttpStatus;
public interface CustomException {
    HttpStatus getStatus();
}
