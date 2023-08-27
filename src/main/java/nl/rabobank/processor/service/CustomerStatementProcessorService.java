package nl.rabobank.processor.service;

import nl.rabobank.processor.api.model.response.GenericResponse;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.exception.EmptyFileException;
import nl.rabobank.processor.processor.FileProcessor;
import nl.rabobank.processor.processor.factory.FileProcessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CustomerStatementProcessorService {
    private final Logger logger = LoggerFactory.getLogger(CustomerStatementProcessorService.class);

    private FileProcessor processor;
    private final FileProcessorFactory fileProcessorFactory;

    public CustomerStatementProcessorService(@Lazy FileProcessor processor,
                                             FileProcessorFactory fileProcessorFactory) {
        this.processor = processor;
        this.fileProcessorFactory = fileProcessorFactory;
    }

    public GenericResponse processCustomerStatement(MultipartFile file) throws IOException {
        logger.info("inside processCustomerStatement");
        String contentType = file.getContentType();

        if (file.isEmpty()) {
            throw new EmptyFileException("File can't be empty");
        }

        processor = fileProcessorFactory.createFileProcessor(contentType);
        List<CustomerStatement> customerStatements = processor.processFile(file);
        List<CustomerStatement> filteredList = customerStatements.stream()
//                .filter(filterNonUniqueStatements(customerStatements))
                .filter(getCustomerStatementPredicate().negate())
                .toList();
        System.out.println(filteredList);

        return null;
    }

    private Predicate<CustomerStatement> filterNonUniqueStatements(List<CustomerStatement> customerStatements) {
        return statement -> Collections.frequency(customerStatements, statement.getReference()) > 1;
    }

    private Predicate<CustomerStatement> getCustomerStatementPredicate() {
        return statement -> statement.getStartBalance().add(statement.getMutation()).compareTo(statement.getEndBalance()) == 0;
    }

}
