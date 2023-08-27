package nl.rabobank.processor.api.model.response;

import lombok.Data;

@Data
public class FailedRecordResponse {
    private int reference;

    private String description;
}
