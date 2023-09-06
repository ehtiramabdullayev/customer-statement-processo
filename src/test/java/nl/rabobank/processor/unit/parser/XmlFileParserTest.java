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
    public void testProcessFile_Success() {
        String xmlContent = """
                <records>
                  <record reference="138932">
                    <accountNumber>NL90ABNA0585647886</accountNumber>
                    <description>Flowers for Richard Bakker</description>
                    <startBalance>94.9</startBalance>
                    <mutation>+14.63</mutation>
                    <endBalance>109.53</endBalance>
                  </record>
                  <record reference="131254">
                    <accountNumber>NL93ABNA0585619023</accountNumber>
                    <description>Candy from Vincent de Vries</description>
                    <startBalance>5429</startBalance>
                    <mutation>-939</mutation>
                    <endBalance>6368</endBalance>
                  </record></records>""";

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

        InvalidUploadException thrown = Assertions.assertThrows(InvalidUploadException.class, () -> xmlFileParser.parseFile(mockFile));
        assertEquals("The XMl file is invalid ", thrown.getLocalizedMessage());

    }
}