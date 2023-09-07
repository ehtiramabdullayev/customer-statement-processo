package nl.rabobank.processor.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.dto.CustomerStatementListXml;
import nl.rabobank.processor.exception.InvalidUploadException;
import nl.rabobank.processor.mapper.CustomerStatementXmlToDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static nl.rabobank.processor.util.Constants.XML_FILE_IS_INVALID;
import static nl.rabobank.processor.util.Constants.XML_PROCESSING_FAILED;
import static nl.rabobank.processor.util.Constants.XML_PROCESSING_STARTED;

@Slf4j
@Service
@Qualifier("XmlFileParser")
public class XmlFileParser implements FileParser {
    private final CustomerStatementXmlToDTOMapper customerStatementXmlToDTOMapper;
    @Autowired
    @Qualifier("xmlMapper")
    private final XmlMapper xmlMapper;

    public XmlFileParser(CustomerStatementXmlToDTOMapper customerStatementXmlToDTOMapper,
                         XmlMapper xmlMapper) {
        this.customerStatementXmlToDTOMapper = customerStatementXmlToDTOMapper;
        this.xmlMapper = xmlMapper;
    }

    @Override
    public List<CustomerStatement> parseFile(MultipartFile file) {
        log.info(XML_PROCESSING_STARTED);
        try {
            CustomerStatementListXml list = xmlMapper.readValue(file.getInputStream(), CustomerStatementListXml.class);
            return customerStatementXmlToDTOMapper.fromXmlToDtoList(list);
        } catch (IOException e) {
            log.error(e.getMessage());
            log.error(XML_PROCESSING_FAILED);
            throw new InvalidUploadException(XML_FILE_IS_INVALID);
        }
    }
}