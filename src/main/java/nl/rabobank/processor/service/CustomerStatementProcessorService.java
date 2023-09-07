package nl.rabobank.processor.service;

import lombok.extern.slf4j.Slf4j;
import nl.rabobank.processor.api.model.response.FailedRecordListResponse;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.mapper.CustomerStatementDtoToResponseMapper;
import nl.rabobank.processor.parser.FileParser;
import nl.rabobank.processor.parser.factory.FileParserFactory;
import nl.rabobank.processor.validator.CustomerStatementValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static nl.rabobank.processor.util.Constants.INSIDE_SERVICE_METHOD;

@Service
@Slf4j
public class CustomerStatementProcessorService {
    private final FileParserFactory fileParserFactory;
    private final CustomerStatementValidator customerStatementValidator;
    private final CustomerStatementDtoToResponseMapper customerStatementDtoToResponseMapper;

    public CustomerStatementProcessorService(FileParserFactory fileParserFactory,
                                             CustomerStatementValidator customerStatementValidator,
                                             CustomerStatementDtoToResponseMapper customerStatementDtoToResponseMapper) {
        this.fileParserFactory = fileParserFactory;
        this.customerStatementValidator = customerStatementValidator;
        this.customerStatementDtoToResponseMapper = customerStatementDtoToResponseMapper;
    }

    public FailedRecordListResponse processCustomerStatement(MultipartFile file) {
        log.info(INSIDE_SERVICE_METHOD);
        customerStatementValidator.validateCustomerStatementFile(file);
        FileParser fileParser = fileParserFactory.createFileProcessor(file);

        List<CustomerStatement> parsedFile = fileParser.parseFile(file);
        List<CustomerStatement> endBalanceFailedRecords = customerStatementValidator.findNonValidatedEndBalance(parsedFile);
        List<CustomerStatement> nonUniqueFailedRecords = customerStatementValidator.findNonUniqueByReference(parsedFile);

        if (!nonUniqueFailedRecords.isEmpty()) endBalanceFailedRecords.addAll(nonUniqueFailedRecords);
        return customerStatementDtoToResponseMapper.fromDtoToListResponse(endBalanceFailedRecords);
    }

}
