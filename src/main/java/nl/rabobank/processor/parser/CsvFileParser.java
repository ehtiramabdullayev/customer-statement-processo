package nl.rabobank.processor.parser;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.dto.CustomerStatementCsv;
import nl.rabobank.processor.exception.InvalidUploadException;
import nl.rabobank.processor.mapper.CustomerStatementCsvToDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static nl.rabobank.processor.util.Constants.CVS_FILE_IS_INVALID;
import static nl.rabobank.processor.util.Constants.CVS_PROCESSING_FAILED;
import static nl.rabobank.processor.util.Constants.CVS_PROCESSING_STARTED;

@Slf4j
@Service
@Qualifier("CsvFileParser")
public class CsvFileParser implements FileParser {
    private final CustomerStatementCsvToDTOMapper customerStatementCsvToDTOMapper;

    @Autowired
    @Qualifier("csvMapper")
    private final CsvMapper csvMapper;

    public CsvFileParser(CustomerStatementCsvToDTOMapper customerStatementCsvToDTOMapper,
                         CsvMapper csvMapper) {
        this.customerStatementCsvToDTOMapper = customerStatementCsvToDTOMapper;
        this.csvMapper = csvMapper;
    }

    @Override
    public List<CustomerStatement> parseFile(MultipartFile file) {
        log.info(CVS_PROCESSING_STARTED);
        try {
            CsvSchema schema = csvMapper
                    .schemaFor(CustomerStatementCsv.class)
                    .withHeader()
                    .withColumnSeparator(CsvSchema.DEFAULT_COLUMN_SEPARATOR);

            try (MappingIterator<CustomerStatementCsv> parsedData = csvMapper.readerWithSchemaFor(CustomerStatementCsv.class)
                    .with(schema)
                    .readValues(new String(file.getBytes(), StandardCharsets.UTF_8))) {
                List<CustomerStatementCsv> csvList = parsedData.readAll();
                return customerStatementCsvToDTOMapper.fromCsvToDtoList(csvList);
            }
        } catch (IOException e) {
            log.error(CVS_PROCESSING_FAILED);
            throw new InvalidUploadException(CVS_FILE_IS_INVALID + e.getLocalizedMessage());
        }
    }
}
