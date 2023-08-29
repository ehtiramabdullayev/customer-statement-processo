package nl.rabobank.processor.unit.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.rabobank.processor.api.model.response.FailedRecordListResponse;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.exception.InvalidUploadException;
import nl.rabobank.processor.mapper.CustomerStatementDtoToResponseMapper;
import nl.rabobank.processor.mapper.CustomerStatementXmlToDTOMapper;
import nl.rabobank.processor.parser.XmlFileParser;
import nl.rabobank.processor.parser.factory.FileParserFactory;
import nl.rabobank.processor.service.CustomerStatementProcessorService;
import nl.rabobank.processor.validator.CustomerStatementValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class CustomerStatementProcessorServiceTest {

    @Mock
    private FileParserFactory fileParserFactory;

    @Mock
    private CustomerStatementValidator customerStatementValidator;

    @Mock
    private CustomerStatementDtoToResponseMapper customerStatementDtoToResponseMapper;

    @InjectMocks
    private CustomerStatementProcessorService processorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessCustomerStatement_Success() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.xml",
                "text/xml",
                "content".getBytes()
        );

        when(customerStatementValidator.findNonValidatedEndBalance(any())).thenReturn(new ArrayList<>());
        when(customerStatementValidator.findNonUniqueByReference(any())).thenReturn(new ArrayList<>());

        FailedRecordListResponse mockResponse = new FailedRecordListResponse();
        when(customerStatementDtoToResponseMapper.fromDtoToListResponse(any(List.class))).thenReturn(mockResponse);

        XmlFileParser xmlFileParser = new XmlFileParser(
                mock(CustomerStatementXmlToDTOMapper.class),
                mock(XmlMapper.class)
        );
        when(fileParserFactory.createFileProcessor(mockFile)).thenReturn(xmlFileParser);

        List<CustomerStatement> mockParsedFile = new ArrayList<>();
        when(xmlFileParser.parseFile(mockFile)).thenReturn(mockParsedFile);

        FailedRecordListResponse result = processorService.processCustomerStatement(mockFile);

        assertEquals(mockResponse, result);

        verify(customerStatementValidator).validateCustomerStatementFile(mockFile);
        verify(fileParserFactory).createFileProcessor(mockFile);
        verify(customerStatementDtoToResponseMapper).fromDtoToListResponse(mockParsedFile);
    }

    @Test
    public void testProcessCustomerStatement_InvalidFile_ThrowsException() throws IOException {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.xml",
                "text/xml",
                "content".getBytes()
        );

        doThrow(InvalidUploadException.class).when(customerStatementValidator).validateCustomerStatementFile(mockFile);

        assertThrows(InvalidUploadException.class, () -> processorService.processCustomerStatement(mockFile));

        verify(customerStatementValidator).validateCustomerStatementFile(mockFile);
        verifyNoInteractions(fileParserFactory, customerStatementDtoToResponseMapper);
    }

}