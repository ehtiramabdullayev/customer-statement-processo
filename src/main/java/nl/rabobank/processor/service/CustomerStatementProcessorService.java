package nl.rabobank.processor.service;

import nl.rabobank.processor.api.model.response.FailedRecordListResponse;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.mapper.CustomerStatementDtoToResponseMapper;
import nl.rabobank.processor.parser.FileParser;
import nl.rabobank.processor.parser.factory.FileParserFactory;
import nl.rabobank.processor.validator.CustomerStatementValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import static nl.rabobank.processor.util.Constants.INSIDE_SERVICE_METHOD;


@Service
public class CustomerStatementProcessorService {
    private final Logger logger = LoggerFactory.getLogger(CustomerStatementProcessorService.class);

    private FileParser fileParser;
    private final FileParserFactory fileParserFactory;
    private final CustomerStatementValidator customerStatementValidator;
    private final CustomerStatementDtoToResponseMapper customerStatementDtoToResponseMapper;

    public CustomerStatementProcessorService(@Lazy FileParser fileParser,
                                             FileParserFactory fileParserFactory,
                                             CustomerStatementValidator customerStatementValidator,
                                             CustomerStatementDtoToResponseMapper customerStatementDtoToResponseMapper) {
        this.fileParser = fileParser;
        this.fileParserFactory = fileParserFactory;
        this.customerStatementValidator = customerStatementValidator;
        this.customerStatementDtoToResponseMapper = customerStatementDtoToResponseMapper;
    }

    public FailedRecordListResponse processCustomerStatement(MultipartFile file) {
        logger.info(INSIDE_SERVICE_METHOD);

        customerStatementValidator.validateCustomerStatementFile(file);

        fileParser = fileParserFactory.createFileProcessor(file);

        List<CustomerStatement> parsedFile = fileParser.processFile(file);
        
        List<CustomerStatement> endBalanceFailedRecords = customerStatementValidator.findNonValidatedEndBalance(parsedFile);
        
        List<CustomerStatement> nonUniqueFailedRecords = customerStatementValidator.findNonUniqueByReference(parsedFile);

        if (!nonUniqueFailedRecords.isEmpty()) endBalanceFailedRecords.addAll(nonUniqueFailedRecords);

        return customerStatementDtoToResponseMapper.fromDtoToListResponse(endBalanceFailedRecords);
    }

}
