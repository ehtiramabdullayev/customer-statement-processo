package nl.rabobank.processor.processor.factory;

import nl.rabobank.processor.exception.UploadTypeNotSupported;
import nl.rabobank.processor.processor.CsvFileProcessor;
import nl.rabobank.processor.processor.FileProcessor;
import nl.rabobank.processor.processor.XmlFileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static nl.rabobank.processor.util.Constants.*;

@Component
public class FileProcessorFactory {
    private final ApplicationContext applicationContext;

    @Autowired
    public FileProcessorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public FileProcessor createFileProcessor(MultipartFile file) {
        String contentType = Objects.isNull(file.getContentType()) ? NOT_SUPPORTED_TYPE : file.getContentType();
        return switch (contentType.toLowerCase()) {
            case CSV -> applicationContext.getBean(CsvFileProcessor.class);
            case XML -> applicationContext.getBean(XmlFileProcessor.class);
            default -> throw new UploadTypeNotSupported("File type is not supported: " + contentType);
        };
    }
}
