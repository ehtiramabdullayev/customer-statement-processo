package nl.rabobank.processor.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.dto.CustomerStatementCsv;
import nl.rabobank.processor.dto.CustomerStatementListXml;
import nl.rabobank.processor.dto.CustomerStatementXml;
import nl.rabobank.processor.mapper.CustomerStatementCsvToDTOMapper;
import nl.rabobank.processor.mapper.CustomerStatementXmlToDTOMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("xmlFileProcessor")
public class XmlFileProcessor implements FileProcessor<CustomerStatement> {

    private final CustomerStatementXmlToDTOMapper customerStatementXmlToDTOMapper;

    public XmlFileProcessor(CustomerStatementXmlToDTOMapper customerStatementXmlToDTOMapper) {
        this.customerStatementXmlToDTOMapper = customerStatementXmlToDTOMapper;
    }

    @Override
    public List<CustomerStatement> processFile(MultipartFile file) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            CustomerStatementListXml list = xmlMapper.readValue(file.getInputStream(), CustomerStatementListXml.class);
            return null;

        } catch (IOException e) {
            throw new RuntimeException("Error parsing XML file", e);
        }
    }
}