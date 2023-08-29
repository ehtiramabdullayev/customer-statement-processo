package nl.rabobank.processor.unit.parser;

import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.mapper.CustomerStatementCsvToDTOMapper;
import nl.rabobank.processor.parser.CsvFileParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CsvFileParserTest {

    @InjectMocks
    private CsvFileParser csvFileParser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        CustomerStatementCsvToDTOMapper customerStatementCsvToDTOMapper = new CustomerStatementCsvToDTOMapper();
        csvFileParser = new CsvFileParser(customerStatementCsvToDTOMapper);
    }

    @Test
    public void testParseFile_Success() throws IOException {
        String csvContent = "Reference,Account Number,Description,Start Balance,Mutation,End Balance\n" +
                "183398,NL56RABO0149876948,Clothes from Richard de Vries,33.34,5.55,38.89\n" +
                "112806,NL27SNSB0917829871,Subscription from Jan Dekker,28.95,-19.44,9.51\n" +
                "110784,NL93ABNA0585619023,Subscription from Richard Bakker,13.89,-46.18,-32.29";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        List<CustomerStatement> result = csvFileParser.parseFile(mockFile);

        assertFalse(result.isEmpty());
        assertEquals(3, result.size());


    }

    @Test
    public void testParseFile_HasEndBalancedFailedRecord() throws IOException {
        // Mocking behavior for the CsvToBean
        String csvContent = "Reference,AccountNumber,Description,StartBalance,Mutation,EndBalance\n" +
                "12345,NL12ABCD1234567890,Description,100.0,-20.0,5.0";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        List<CustomerStatement> result = csvFileParser.parseFile(mockFile);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}