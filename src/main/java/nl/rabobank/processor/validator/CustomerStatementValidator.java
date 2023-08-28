package nl.rabobank.processor.validator;

import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.exception.EmptyFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static nl.rabobank.processor.util.Constants.*;

@Component
public class CustomerStatementValidator {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void validateCustomerStatementFile(MultipartFile file) {
        logger.info(VALIDATING_CUSTOMER_STATEMENT_FILE);

        if (file.isEmpty()) {
            logger.error(UPLOADED_FILE_IS_EMPTY);
            throw new EmptyFileException(FILE_CANT_BE_EMPTY);
        }
    }

    public Predicate<CustomerStatement> validateEndBalance() {
        logger.info(VALIDATING_END_BALANCE);
        return statement -> statement.getStartBalance().add(statement.getMutation()).compareTo(statement.getEndBalance()) == 0;
    }

    public List<CustomerStatement> findNonValidatedEndBalance(List<CustomerStatement> customerStatements) {
        logger.info(COLLECTING_FAILED_END_BALANCE_RECORDS);
        return customerStatements.stream()
                .filter(validateEndBalance().negate())
                .collect(Collectors.toList());
    }

    public List<CustomerStatement> findNonUniqueByReference(List<CustomerStatement> customerStatements) {
        logger.info(COLLECTING_NON_UNIQUE_RECORDS);

        Map<Integer, List<CustomerStatement>> groupedByReference = customerStatements.stream()
                .collect(Collectors.groupingBy(CustomerStatement::getReference));

        return groupedByReference.values().stream()
                .filter(list -> list.size() > 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
