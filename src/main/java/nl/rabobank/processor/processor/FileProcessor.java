package nl.rabobank.processor.processor;

import nl.rabobank.processor.dto.CustomerStatement;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileProcessor {
    List<CustomerStatement> processFile(MultipartFile file);
}
