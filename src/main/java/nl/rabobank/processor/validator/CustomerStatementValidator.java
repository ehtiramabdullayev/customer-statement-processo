package nl.rabobank.processor.validator;

import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.exception.EmptyFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class CustomerStatementValidator {
    public void validateCustomerStatementFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException("File can't be empty");
        }
    }

    public Predicate<CustomerStatement> validateEndBalance() {
        return statement -> statement.getStartBalance().add(statement.getMutation()).compareTo(statement.getEndBalance()) == 0;
    }


    public List<CustomerStatement> findNonUniqueByReference(List<CustomerStatement> customerStatements) {
        Map<Integer, List<CustomerStatement>> groupedByReference = customerStatements.stream()
                .collect(Collectors.groupingBy(CustomerStatement::getReference));

        List<CustomerStatement> nonUniqueStatements = groupedByReference.values().stream()
                .filter(list -> list.size() > 1)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return nonUniqueStatements;
    }
}
