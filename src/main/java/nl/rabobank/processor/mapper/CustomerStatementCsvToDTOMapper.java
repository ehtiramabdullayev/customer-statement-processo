package nl.rabobank.processor.mapper;

import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.dto.CustomerStatementCsv;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerStatementCsvToDTOMapper {
    private CustomerStatement fromCsvToDTO(CustomerStatementCsv csv) {
        CustomerStatement dto = new CustomerStatement();
        dto.setReference(csv.getReference());
        dto.setAccountNumber(csv.getAccountNumber());
        dto.setDescription(csv.getDescription());
        dto.setStartBalance(csv.getStartBalance());
        dto.setMutation(csv.getMutation());
        dto.setEndBalance(csv.getEndBalance());
        return dto;
    }

    public List<CustomerStatement> fromCsvToDtoList(List<CustomerStatementCsv> csvList) {
        return csvList.stream()
                .map(this::fromCsvToDTO)
                .toList();
    }

}
