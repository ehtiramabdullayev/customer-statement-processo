package nl.rabobank.processor.config;

import com.opencsv.exceptions.CsvException;
import nl.rabobank.processor.api.model.response.GenericResponse;
import nl.rabobank.processor.exception.EmptyFileException;
import nl.rabobank.processor.exception.InvalidUploadException;
import nl.rabobank.processor.exception.UploadTypeNotSupported;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(UploadTypeNotSupported.class)
    @ResponseBody
    public ResponseEntity<GenericResponse> handleUploadTypeNotSupported(UploadTypeNotSupported ex) {
        HttpStatus status = ex.getStatus() == null ? HttpStatus.NOT_FOUND : ex.getStatus();
        return buildResponse(ex.getMessage(), status);
    }

    @ExceptionHandler(InvalidUploadException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse> handleUploadInvalid(InvalidUploadException ex) {
        HttpStatus status = ex.getStatus() == null ? HttpStatus.NOT_FOUND : ex.getStatus();
        return buildResponse(ex.getMessage(), status);
    }

    @ExceptionHandler(EmptyFileException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse> handleEmptyFileException(EmptyFileException ex) {
        HttpStatus status = ex.getStatus() == null ? HttpStatus.NOT_FOUND : ex.getStatus();
        return buildResponse(ex.getMessage(), status);
    }

    @ExceptionHandler(CsvException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse> handleCsvException(CsvException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return buildResponse(ex.getMessage(), status);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<GenericResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        String errorMessage = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return buildResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }


    private ResponseEntity<GenericResponse> buildResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(new GenericResponse(message), status);
    }
}
