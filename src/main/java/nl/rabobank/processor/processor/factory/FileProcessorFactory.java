package nl.rabobank.processor.processor.factory;

import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.processor.FileProcessor;
import org.springframework.stereotype.Component;

@Component
public interface FileProcessorFactory {
    FileProcessor<CustomerStatement> createFileProcessor(String fileType);

}
