package nl.rabobank.processor.processor.factory;

import nl.rabobank.processor.dto.CustomerStatementCsv;
import nl.rabobank.processor.processor.FileProcessor;
import org.springframework.stereotype.Component;

@Component
public interface FileProcessorFactory {
    FileProcessor<CustomerStatementCsv> createFileProcessor(String fileType);

}
