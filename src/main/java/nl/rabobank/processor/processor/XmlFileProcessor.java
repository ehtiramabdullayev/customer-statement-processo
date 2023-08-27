package nl.rabobank.processor.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.rabobank.processor.dto.CustomerStatementCsv;
import nl.rabobank.processor.dto.CustomerStatementListXml;
import nl.rabobank.processor.dto.CustomerStatementXml;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Qualifier("xmlFileProcessor")
public class XmlFileProcessor implements FileProcessor<CustomerStatementXml> {

    @Override
    public List<CustomerStatementXml> processFile(MultipartFile file) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            CustomerStatementListXml list = xmlMapper.readValue(file.getInputStream(), CustomerStatementListXml.class);
            return list.getRecords();
        } catch (IOException e) {
            throw new RuntimeException("Error parsing XML file", e);
        }
    }
}