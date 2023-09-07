package nl.rabobank.processor.api.model.response;

import lombok.Data;
@Data
public class FailedRecordResponse {
    private long reference;
    private String description;
}
