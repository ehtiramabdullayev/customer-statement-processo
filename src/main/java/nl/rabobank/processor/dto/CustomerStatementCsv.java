package nl.rabobank.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JacksonXmlRootElement
@JsonPropertyOrder({"Reference", "Account Number", "Description", "Start Balance", "Mutation", "End Balance"})
public class CustomerStatementCsv {
    @JsonProperty("Reference")
    private int reference;

    @JsonProperty("Account Number")
    private String accountNumber;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Start Balance")
    private BigDecimal startBalance;

    @JsonProperty("Mutation")
    private BigDecimal mutation;

    @JsonProperty("End Balance")
    private BigDecimal endBalance;
}
