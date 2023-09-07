package nl.rabobank.processor.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class CustomerStatement {
    private int reference;
    private String accountNumber;
    private String description;
    private BigDecimal startBalance;
    private BigDecimal mutation;
    private BigDecimal endBalance;
}
