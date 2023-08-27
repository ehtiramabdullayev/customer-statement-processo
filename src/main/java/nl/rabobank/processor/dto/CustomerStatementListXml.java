package nl.rabobank.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "records")
public class CustomerStatementListXml {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "record")
    private List<CustomerStatementXml> records;

    public List<CustomerStatementXml> getRecords() {
        return records;
    }

    public void setRecords(List<CustomerStatementXml> records) {
        this.records = records;
    }
}
