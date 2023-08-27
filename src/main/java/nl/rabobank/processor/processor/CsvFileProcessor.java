package nl.rabobank.processor.processor;

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

import java.io.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Qualifier("csvFileProcessor")
public class CsvFileProcessor implements FileProcessor<CustomerStatement> {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CustomerStatementCsvToDTOMapper customerStatementCsvToDTOMapper;

    public CsvFileProcessor(CustomerStatementCsvToDTOMapper customerStatementCsvToDTOMapper) {
        this.customerStatementCsvToDTOMapper = customerStatementCsvToDTOMapper;
    }

    @Override
    public List<CustomerStatement> processFile(MultipartFile file) {
        try {
            Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CsvToBean<CustomerStatementCsv> csvToBean =
                    new CsvToBeanBuilder<CustomerStatementCsv>(reader)
                            .withType(CustomerStatementCsv.class)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            List<CustomerStatementCsv> csvList = csvToBean.stream().filter(exclude().negate()).toList();

            return customerStatementCsvToDTOMapper.fromCsvToDtoList(csvList);

        } catch (IOException e) {
            throw new InvalidUploadException("The CSV file is invalid " + e.getLocalizedMessage());
        }
    }

    private Predicate<CustomerStatementCsv> exclude() {
        //Exclude all
        return customerStatement -> {
            return false;

        };
    }
}
