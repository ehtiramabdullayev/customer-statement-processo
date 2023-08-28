package nl.rabobank.processor.validator;

import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.exception.EmptyFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static nl.rabobank.processor.util.Constants.FILE_CANT_BE_EMPTY;

@Component
public class CustomerStatementValidator {
    public void validateCustomerStatementFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException(FILE_CANT_BE_EMPTY);
        }
    }

    public Predicate<CustomerStatement> validateEndBalance() {
        return statement -> statement.getStartBalance().add(statement.getMutation()).compareTo(statement.getEndBalance()) == 0;
    }

    public List<CustomerStatement> findNonValidatedEndBalance(List<CustomerStatement> customerStatements) {
        return customerStatements.stream()
                .filter(validateEndBalance().negate())
                .collect(Collectors.toList());
    }

    public List<CustomerStatement> findNonUniqueByReference(List<CustomerStatement> customerStatements) {
        Map<Integer, List<CustomerStatement>> groupedByReference = customerStatements.stream()
                .collect(Collectors.groupingBy(CustomerStatement::getReference));

        return groupedByReference.values().stream()
                .filter(list -> list.size() > 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
