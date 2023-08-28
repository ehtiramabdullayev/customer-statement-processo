package nl.rabobank.processor.parser.factory;

import nl.rabobank.processor.exception.UploadTypeNotSupported;
import nl.rabobank.processor.parser.CsvFileParser;
import nl.rabobank.processor.parser.FileParser;
import nl.rabobank.processor.parser.XmlFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static nl.rabobank.processor.util.Constants.*;

@Component
public class FileParserFactory {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ApplicationContext applicationContext;

    @Autowired
    public FileParserFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public FileParser createFileProcessor(MultipartFile file) {
        logger.info(CREATING_FILE_PROCESSOR);
        String contentType = Objects.isNull(file.getContentType()) ? NOT_SUPPORTED_TYPE : file.getContentType();
        return switch (contentType.toLowerCase()) {
            case CSV -> applicationContext.getBean(CsvFileParser.class);
            case XML -> applicationContext.getBean(XmlFileParser.class);
            default -> throw new UploadTypeNotSupported(FILE_TYPE_NOT_SUPPORTED + contentType);
        };
    }
}
