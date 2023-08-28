package nl.rabobank.processor.processor;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.dto.CustomerStatementListXml;
import nl.rabobank.processor.exception.InvalidUploadException;
import nl.rabobank.processor.mapper.CustomerStatementXmlToDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static nl.rabobank.processor.util.Constants.*;

@Service
@Qualifier("xmlFileProcessor")
public class XmlFileProcessor implements FileProcessor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomerStatementXmlToDTOMapper customerStatementXmlToDTOMapper;
    private final XmlMapper xmlMapper;

    public XmlFileProcessor(CustomerStatementXmlToDTOMapper customerStatementXmlToDTOMapper,
                            XmlMapper xmlMapper) {
        this.customerStatementXmlToDTOMapper = customerStatementXmlToDTOMapper;
        this.xmlMapper = xmlMapper;
    }

    @Override
    public List<CustomerStatement> processFile(MultipartFile file) {
        logger.info(XML_PROCESSING_STARTED);
        try {
            CustomerStatementListXml list = xmlMapper.readValue(file.getInputStream(), CustomerStatementListXml.class);
            return customerStatementXmlToDTOMapper.fromXmlToDtoList(list);
        } catch (IOException e) {
            logger.error(XML_PROCESSING_FAILED);
            throw new InvalidUploadException(XML_FILE_IS_INVALID + e.getLocalizedMessage());
        }
    }
}