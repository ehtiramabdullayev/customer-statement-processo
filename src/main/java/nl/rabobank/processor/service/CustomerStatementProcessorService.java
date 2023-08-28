package nl.rabobank.processor.service;

import nl.rabobank.processor.api.model.response.FailedRecordListResponse;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.mapper.CustomerStatementDtoToResponseMapper;
import nl.rabobank.processor.processor.FileProcessor;
import nl.rabobank.processor.processor.factory.FileProcessorFactory;
import nl.rabobank.processor.validator.CustomerStatementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class CustomerStatementProcessorService {
    private final Logger logger = LoggerFactory.getLogger(CustomerStatementProcessorService.class);

    private FileProcessor processor;
    private final FileProcessorFactory fileProcessorFactory;
    private final CustomerStatementValidator customerStatementValidator;
    private final CustomerStatementDtoToResponseMapper customerStatementDtoToResponseMapper;

    public CustomerStatementProcessorService(@Lazy FileProcessor processor,
                                             FileProcessorFactory fileProcessorFactory,
                                             CustomerStatementValidator customerStatementValidator,
                                             CustomerStatementDtoToResponseMapper customerStatementDtoToResponseMapper) {
        this.processor = processor;
        this.fileProcessorFactory = fileProcessorFactory;
        this.customerStatementValidator = customerStatementValidator;
        this.customerStatementDtoToResponseMapper = customerStatementDtoToResponseMapper;
    }

    public FailedRecordListResponse processCustomerStatement(MultipartFile file) {
        logger.info("inside processCustomerStatement");
        String contentType = file.getContentType();

        customerStatementValidator.validateCustomerStatementFile(file);

        processor = fileProcessorFactory.createFileProcessor(contentType);

        List<CustomerStatement> customerStatements = processor.processFile(file);
        
        List<CustomerStatement> endBalanceFailedRecords = new ArrayList<>(customerStatements.stream()
                .filter(customerStatementValidator.validateEndBalance().negate())
                .toList());
        
        List<CustomerStatement> nonUniqueFailedRecords = customerStatementValidator.findNonUniqueByReference(customerStatements);

        if (!nonUniqueFailedRecords.isEmpty()) endBalanceFailedRecords.addAll(nonUniqueFailedRecords);

        return customerStatementDtoToResponseMapper.fromDtoToListResponse(endBalanceFailedRecords);
    }

}
