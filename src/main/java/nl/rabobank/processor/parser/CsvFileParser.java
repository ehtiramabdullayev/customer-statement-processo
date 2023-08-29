package nl.rabobank.processor.parser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.dto.CustomerStatementCsv;
import nl.rabobank.processor.exception.InvalidUploadException;
import nl.rabobank.processor.mapper.CustomerStatementCsvToDTOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import static nl.rabobank.processor.util.Constants.*;

@Service
@Qualifier("csvFileProcessor")
public class CsvFileParser implements FileParser {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CustomerStatementCsvToDTOMapper customerStatementCsvToDTOMapper;

    public CsvFileParser(CustomerStatementCsvToDTOMapper customerStatementCsvToDTOMapper) {
        this.customerStatementCsvToDTOMapper = customerStatementCsvToDTOMapper;
    }

    @Override
    public List<CustomerStatement> parseFile(MultipartFile file) {
        logger.info(CVS_PROCESSING_STARTED);
        try {
            List<CustomerStatementCsv> csvList;
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                CsvToBean<CustomerStatementCsv> csvToBean =
                        new CsvToBeanBuilder<CustomerStatementCsv>(reader)
                                .withType(CustomerStatementCsv.class)
                                .withIgnoreLeadingWhiteSpace(true)
                                .build();
                csvList = csvToBean.stream().toList();
            }
            return customerStatementCsvToDTOMapper.fromCsvToDtoList(csvList);
        } catch (IOException e) {
            logger.error(CVS_PROCESSING_FAILED);
            throw new InvalidUploadException(CVS_FILE_IS_INVALID + e.getLocalizedMessage());
        }
    }
}
