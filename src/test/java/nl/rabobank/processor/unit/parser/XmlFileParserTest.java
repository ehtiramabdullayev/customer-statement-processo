package nl.rabobank.processor.unit.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.exception.InvalidUploadException;
import nl.rabobank.processor.mapper.CustomerStatementXmlToDTOMapper;
import nl.rabobank.processor.parser.XmlFileParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XmlFileParserTest {

    @InjectMocks
    private XmlFileParser xmlFileParser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        XmlMapper xmlMapper = new XmlMapper();
        CustomerStatementXmlToDTOMapper customerStatementXmlToDTOMapper = new CustomerStatementXmlToDTOMapper();
        xmlFileParser = new XmlFileParser(customerStatementXmlToDTOMapper, xmlMapper);
    }

    @Test
    public void testProcessFile_Success() throws IOException {
        String xmlContent = "<records>\n" +
                "  <record reference=\"138932\">\n" +
                "    <accountNumber>NL90ABNA0585647886</accountNumber>\n" +
                "    <description>Flowers for Richard Bakker</description>\n" +
                "    <startBalance>94.9</startBalance>\n" +
                "    <mutation>+14.63</mutation>\n" +
                "    <endBalance>109.53</endBalance>\n" +
                "  </record>\n" +
                "  <record reference=\"131254\">\n" +
                "    <accountNumber>NL93ABNA0585619023</accountNumber>\n" +
                "    <description>Candy from Vincent de Vries</description>\n" +
                "    <startBalance>5429</startBalance>\n" +
                "    <mutation>-939</mutation>\n" +
                "    <endBalance>6368</endBalance>\n" +
                "  </record>" +
                "</records>";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.xml",
                "text/xml",
                xmlContent.getBytes()
        );

        List<CustomerStatement> result = xmlFileParser.parseFile(mockFile);
        assertEquals(2, result.size());
    }

    @Test
    public void testProcessFile_Fail() {
        String xmlContent = "<records>" +
                "<invalid></invalid>\n" +
                "</records>";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.xml",
                "text/xml",
                xmlContent.getBytes()
        );

        InvalidUploadException thrown = Assertions.assertThrows(InvalidUploadException.class, () -> {
            List<CustomerStatement> result = xmlFileParser.parseFile(mockFile);
        });
        assertEquals("The XMl file is invalid ", thrown.getLocalizedMessage());

    }
}