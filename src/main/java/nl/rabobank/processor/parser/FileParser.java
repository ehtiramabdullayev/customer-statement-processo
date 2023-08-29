package nl.rabobank.processor.parser;

import nl.rabobank.processor.dto.CustomerStatement;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileParser {
    List<CustomerStatement> parseFile(MultipartFile file);
}
