package nl.rabobank.processor.processor.factory;

import nl.rabobank.processor.processor.FileProcessor;
import org.springframework.stereotype.Component;

@Component
public interface FileProcessorFactory {
    FileProcessor createFileProcessor(String fileType);
}
