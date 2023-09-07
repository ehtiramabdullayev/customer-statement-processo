package nl.rabobank.processor.mapper;

import nl.rabobank.processor.api.model.response.FailedRecordListResponse;
import nl.rabobank.processor.api.model.response.FailedRecordResponse;
import nl.rabobank.processor.dto.CustomerStatement;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class CustomerStatementDtoToResponseMapper {
    private FailedRecordResponse fromDtoToResponse(CustomerStatement statement) {
        FailedRecordResponse response = new FailedRecordResponse();
        response.setReference(statement.getReference());
        response.setDescription(statement.getDescription());
        return response;
    }

    public FailedRecordListResponse fromDtoToListResponse(List<CustomerStatement> statements) {
        FailedRecordListResponse response = new FailedRecordListResponse();
        List<FailedRecordResponse> reportList = statements.stream()
                .map(this::fromDtoToResponse)
                .toList();
        response.setReportResponseList(reportList);
        return response;
    }
}
