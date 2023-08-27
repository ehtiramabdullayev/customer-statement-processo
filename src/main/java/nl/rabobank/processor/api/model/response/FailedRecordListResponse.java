package nl.rabobank.processor.api.model.response;

import lombok.Data;

import java.util.List;

@Data
public class FailedRecordListResponse {
    private List<FailedRecordResponse> reportResponseList;
}
