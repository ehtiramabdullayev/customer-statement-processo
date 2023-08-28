package nl.rabobank.processor.controller;

import nl.rabobank.processor.api.model.response.FailedRecordListResponse;
import nl.rabobank.processor.service.CustomerStatementProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import static nl.rabobank.processor.util.Constants.INSIDE_CONTROLLER_ENDPOINT;

//@Api(value = "IngredientController", tags = "Ingredient Controller", description = "Create, update, delete, list ingredients")
@RestController
@RequestMapping(value = "api/v1/process")
public class ProcessingController {
    private final CustomerStatementProcessorService customerStatementProcessorService;
    private final Logger logger = LoggerFactory.getLogger(ProcessingController.class);


    public ProcessingController(CustomerStatementProcessorService customerStatementProcessorService) {
        this.customerStatementProcessorService = customerStatementProcessorService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FailedRecordListResponse uploadFile(@NonNull @RequestParam("file") MultipartFile file) {
        logger.info(INSIDE_CONTROLLER_ENDPOINT);
        return customerStatementProcessorService.processCustomerStatement(file);
    }
}
