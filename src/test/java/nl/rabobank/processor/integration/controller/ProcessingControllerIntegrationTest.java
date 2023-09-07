package nl.rabobank.processor.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProcessingControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUploadFileWithValidXmlSuccess() throws Exception {
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

        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "application/xml", xmlContent.getBytes());

        mockMvc.perform(multipart("/api/v1/process").file(file)).andExpect(status().isOk());
    }

    @Test
    public void testUploadFileWithInvalidXmlFail() throws Exception {
        String xmlContent = """
                <records>
                  <record reference="138932">
                    <accountNumber>NL90ABNA0585647886</accountNumber>
                   """;

        MockMultipartFile file = new MockMultipartFile("file", "test.xml", "application/xml", xmlContent.getBytes());

        mockMvc.perform(multipart("/api/v1/process").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }


    @Test
    public void testUploadFileWithValidCsvSuccess() throws Exception {
        String csvContent = """
                Reference,Account Number,Description,Start Balance,Mutation,End Balance
                183398,NL56RABO0149876948,Clothes from Richard de Vries,33.34,5.55,38.89
                112806,NL27SNSB0917829871,Subscription from Jan Dekker,28.95,-19.44,9.51
                110784,NL93ABNA0585619023,Subscription from Richard Bakker,13.89,-46.18,-32.29""";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        mockMvc.perform(multipart("/api/v1/process").file(file)).andExpect(status().isOk());
    }

    @Test
    public void testUploadFileWithValidCsvWithNotCorrectEndBalanceFail() throws Exception {
        String csvContent = "Reference,AccountNumber,Description,StartBalance,Mutation,EndBalance\n" +
                "12345,NL12ABCD1234567890,Description,100.0,-20.0,5.0";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        mockMvc.perform(multipart("/api/v1/process").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportResponseList").exists());
    }

    @Test
    public void testUploadFileWithInvalidCsvFileFail() throws Exception {
        String csvContent = "Reference,AccountNumber,test,StartBalance,Mutation,EndBalance\n" +
                "12345,fail, false, invalid, ";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                csvContent.getBytes()
        );

        mockMvc.perform(multipart("/api/v1/process").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}
