package nl.rabobank.processor.controller;

import nl.rabobank.processor.api.model.response.FailedRecordListResponse;
import nl.rabobank.processor.service.CustomerStatementProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//@Api(value = "IngredientController", tags = "Ingredient Controller", description = "Create, update, delete, list ingredients")
@RestController
@RequestMapping(value = "api/v1/process")
public class ProcessingController {
    private final CustomerStatementProcessorService customerStatementProcessorService;
    private final Logger logger = LoggerFactory.getLogger(ProcessingController.class);


    public ProcessingController(CustomerStatementProcessorService customerStatementProcessorService) {
        this.customerStatementProcessorService = customerStatementProcessorService;
    }

    @PostMapping
    public FailedRecordListResponse uploadFile(@NonNull @RequestParam("file") MultipartFile file) {
        logger.info("Inside uploadFile function");
        return customerStatementProcessorService.processCustomerStatement(file);
    }
}
