package nl.rabobank.processor.unit.controller;

import nl.rabobank.processor.api.model.response.FailedRecordListResponse;
import nl.rabobank.processor.controller.ProcessingController;
import nl.rabobank.processor.service.CustomerStatementProcessorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProcessingControllerTest {
    @Mock
    private CustomerStatementProcessorService customerStatementProcessorService;

    @InjectMocks
    private ProcessingController processingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadFile_Success() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                "content".getBytes()
        );

        FailedRecordListResponse expectedResponse = new FailedRecordListResponse();
        when(customerStatementProcessorService.processCustomerStatement(mockFile)).thenReturn(expectedResponse);

        FailedRecordListResponse response = processingController.uploadFile(mockFile);

        assertEquals(expectedResponse, response);

        verify(customerStatementProcessorService).processCustomerStatement(mockFile);
    }
}