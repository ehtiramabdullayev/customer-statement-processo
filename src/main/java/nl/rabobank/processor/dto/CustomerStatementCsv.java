package nl.rabobank.processor.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;


import java.math.BigDecimal;

@Data
public class CustomerStatementCsv {
    @CsvBindByName(column = "Reference")
    private int reference;

    @CsvBindByName(column = "Account Number")
    private String accountNumber;

    @CsvBindByName(column = "Description")
    private String description;

    @CsvBindByName(column = "Start Balance")
    private BigDecimal startBalance;

    @CsvBindByName(column = "Mutation")
    private BigDecimal mutation;

    @CsvBindByName(column = "End Balance")
    private BigDecimal endBalance;
}
