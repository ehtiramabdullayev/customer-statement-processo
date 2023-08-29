package nl.rabobank.processor.unit.validator;

import nl.rabobank.processor.dto.CustomerStatement;
import nl.rabobank.processor.exception.EmptyFileException;
import nl.rabobank.processor.validator.CustomerStatementValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerStatementValidatorTest {

    @InjectMocks
    private CustomerStatementValidator customerStatementValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateCustomerStatementFile_Success() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                "content".getBytes()
        );

        assertDoesNotThrow(() -> customerStatementValidator.validateCustomerStatementFile(mockFile));
    }

    @Test
    public void testValidateEndBalance_Success() {
        Predicate<CustomerStatement> endBalanceValidator = customerStatementValidator.validateEndBalance();

        CustomerStatement statement = new CustomerStatement();
        statement.setStartBalance(new BigDecimal("100.0"));
        statement.setMutation(new BigDecimal("-10.0"));
        statement.setEndBalance(new BigDecimal("90.0"));

        assertTrue(endBalanceValidator.test(statement));
    }

    @Test
    public void testFindNonValidatedEndBalance_Success() {
        Predicate<CustomerStatement> endBalanceValidator = customerStatementValidator.validateEndBalance();

        CustomerStatement statement1 = new CustomerStatement();
        statement1.setStartBalance(new BigDecimal("100.0"));
        statement1.setMutation(new BigDecimal("-10.0"));
        statement1.setEndBalance(new BigDecimal("90.0"));

        CustomerStatement statement2 = new CustomerStatement();
        statement2.setStartBalance(new BigDecimal("200.0"));
        statement2.setMutation(new BigDecimal("10.0"));
        statement2.setEndBalance(new BigDecimal("210.0"));

        List<CustomerStatement> statements = Arrays.asList(statement1, statement2);

        List<CustomerStatement> nonValidatedStatements = customerStatementValidator.findNonValidatedEndBalance(statements);

        assertTrue(nonValidatedStatements.isEmpty());
    }

    @Test
    public void testFindNonUniqueByReference_Success() {
        CustomerStatement statement1 = new CustomerStatement();
        statement1.setReference(123);

        CustomerStatement statement2 = new CustomerStatement();
        statement2.setReference(456);

        CustomerStatement statement3 = new CustomerStatement();
        statement3.setReference(123);

        List<CustomerStatement> statements = Arrays.asList(statement1, statement2, statement3);

        List<CustomerStatement> nonUniqueStatements = customerStatementValidator.findNonUniqueByReference(statements);

        assertEquals(2, nonUniqueStatements.size());
    }

    @Test
    public void testValidateCustomerStatementFile_EmptyFile() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                new byte[0]
        );

        assertThrows(EmptyFileException.class, () -> customerStatementValidator.validateCustomerStatementFile(mockFile));
    }

    @Test
    public void testValidateEndBalance_Failure() {
        Predicate<CustomerStatement> endBalanceValidator = customerStatementValidator.validateEndBalance();

        CustomerStatement statement = new CustomerStatement();
        statement.setStartBalance(new BigDecimal("100.0"));
        statement.setMutation(new BigDecimal("-10.0"));
        statement.setEndBalance(new BigDecimal("80.0"));

        assertFalse(endBalanceValidator.test(statement));
    }

    @Test
    public void testFindNonValidatedEndBalance_Failure() {
        Predicate<CustomerStatement> endBalanceValidator = customerStatementValidator.validateEndBalance();

        CustomerStatement statement1 = new CustomerStatement();
        statement1.setStartBalance(new BigDecimal("100.0"));
        statement1.setMutation(new BigDecimal("-10.0"));
        statement1.setEndBalance(new BigDecimal("90.0"));

        CustomerStatement statement2 = new CustomerStatement();
        statement2.setStartBalance(new BigDecimal("200.0"));
        statement2.setMutation(new BigDecimal("10.0"));
        statement2.setEndBalance(new BigDecimal("220.0"));

        List<CustomerStatement> statements = Arrays.asList(statement1, statement2);

        List<CustomerStatement> nonValidatedStatements = customerStatementValidator.findNonValidatedEndBalance(statements);

        assertEquals(1, nonValidatedStatements.size());
    }

    @Test
    public void testFindNonUniqueByReference_Failure() {
        CustomerStatement statement1 = new CustomerStatement();
        statement1.setReference(123);

        CustomerStatement statement2 = new CustomerStatement();
        statement2.setReference(456);

        CustomerStatement statement3 = new CustomerStatement();
        statement3.setReference(789);

        List<CustomerStatement> statements = Arrays.asList(statement1, statement2, statement3);

        List<CustomerStatement> nonUniqueStatements = customerStatementValidator.findNonUniqueByReference(statements);

        assertTrue(nonUniqueStatements.isEmpty());
    }
}