package nl.rabobank.processor.processor.factory;

import nl.rabobank.processor.dto.CustomerStatementCsv;
import nl.rabobank.processor.exception.UploadTypeNotSupported;
import nl.rabobank.processor.processor.CsvFileProcessor;
import nl.rabobank.processor.processor.FileProcessor;
import nl.rabobank.processor.processor.XmlFileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileProcessorFactoryImpl implements FileProcessorFactory {

    public static final String CSV = "text/csv";
    public static final String XML = "application/xml";

    private final ApplicationContext applicationContext;

    @Autowired
    public FileProcessorFactoryImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public FileProcessor createFileProcessor(String fileType) {
        return switch (fileType.toLowerCase()) {
            case CSV -> applicationContext.getBean(CsvFileProcessor.class);
            case XML -> applicationContext.getBean(XmlFileProcessor.class);
            default -> throw new UploadTypeNotSupported("File type is not supported: " + fileType);
        };
    }
}
