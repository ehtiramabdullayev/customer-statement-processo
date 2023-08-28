package nl.rabobank.processor.mapper;

import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.dto.CustomerStatementListXml;
import nl.rabobank.processor.dto.CustomerStatementXml;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerStatementXmlToDTOMapper {
    private CustomerStatement fromXmlToDTO(CustomerStatementXml xml) {
        CustomerStatement dto = new CustomerStatement();
        dto.setReference(xml.getReference());
        dto.setAccountNumber(xml.getAccountNumber());
        dto.setDescription(xml.getDescription());
        dto.setStartBalance(xml.getStartBalance());
        dto.setMutation(xml.getMutation());
        dto.setEndBalance(xml.getEndBalance());
        return dto;
    }

    public List<CustomerStatement> fromXmlToDtoList(CustomerStatementListXml statementListXml) {
        if (statementListXml.getRecords() == null) return new ArrayList<>();
        return statementListXml.getRecords().stream()
                .map(this::fromXmlToDTO)
                .toList();
    }
}
