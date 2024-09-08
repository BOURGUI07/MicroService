package com.example.accounts.controller;

import com.example.accounts.dto.*;
import com.example.accounts.service.CustomerService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/customers",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Validated
@Tag(name="Customer", description=" Customer Controller")
@Slf4j
public class CustomerController {
    @Value("${build.version}")
    @NonFinal String buildVersion;
    CustomerService customerService;
    Environment environment;
    AccountsContactInfo accountsContactInfo;

    @Operation(summary="Create a new  Customer")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="Customer is successfully created",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerResponse.class)) }),
            @ApiResponse(responseCode="400", description="Client Entered a non Valid Entity Body",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> createAccount(
            @Valid @RequestBody CustomerRequest customerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(customerRequest));
    }



    @Operation(summary="Update customer")
    @ApiResponses(value={
            @ApiResponse(responseCode="404", description="customer isn't found",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode="200", description="customer was successfully Updated",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerResponse.class)) }),
            @ApiResponse(responseCode="400", description="Client Entered a Negative id Or "
                    + "a Non Valid Entity Body",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @PutMapping(value="/{customerId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerResponse> updateAccount(
            @PathVariable Integer customerId,
            @RequestBody @Valid CustomerRequest customerRequest) {

        return ResponseEntity.status(HttpStatus.OK).body(customerService.update(customerId,customerRequest));
    }


    @Operation(summary="Delete customer By phone")
    @ApiResponses(value={
            @ApiResponse(responseCode="404", description="customer isn't found",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode="204", description="customer was successfully Deleted",
                    content = @Content),
            @ApiResponse(responseCode="400", description="Client Entered a non-valid phone number",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@RequestParam @Valid String phone) {
        customerService.deleteByPhone(phone);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary="Get customer by phone", description="Retrieve a single customer by phone")
    @ApiResponses(value={
            @ApiResponse(responseCode="404", description="customer isn't found",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode="200", description="customer was successfully Found",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerResponse.class)) }),
            @ApiResponse(responseCode="400", description="Client Entered a Negative id",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<CustomerResponse> findByPhone(@RequestParam @Valid String phone){
        return ResponseEntity.ok(customerService.findByPhone(phone));
    }


    @Operation(summary="Get customer details by phone", description="Retrieve a all customer details by phone")
    @ApiResponses(value={
            @ApiResponse(responseCode="404", description="customer isn't found",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode="200", description="customer was successfully Found",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CustomerResponse.class)) }),
            @ApiResponse(responseCode="400", description="Client Entered a Negative id",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping("/details")
    public ResponseEntity<CustomerDetailsResponse> findAllDetailsByPhone(@RequestParam @Valid String phone){
        return ResponseEntity.ok(customerService.findAllDetailsByPhone(phone));
    }



    @Operation(summary = "Get Build Information")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Build Information Successfully Retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping("/build-info")
    @Retry(name="buildInfo",fallbackMethod = "buildInfoFallBack")
    public ResponseEntity<String> buildInfo(){
        log.debug("buildInfo() method invoked");
        return ResponseEntity.ok(buildVersion);
    }

    private ResponseEntity<String> buildInfoFallBack(Throwable throwable){
        log.debug("buildInfoFallBack() method invoked");
        return ResponseEntity.ok("0.9");
    }

    @Operation(summary = "Get Java Version")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Java Version Successfully Retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping("/java-version")
    @RateLimiter(name="javaVersion",fallbackMethod = "javaVersionFallBack")
    public ResponseEntity<String> javaVersion(){
        log.debug("javaVersion() method invoked");
        return ResponseEntity.ok(environment.getProperty("JAVA_HOME"));
    }

    private ResponseEntity<String> javaVersionFallBack(Throwable throwable){
        log.debug("javaVersionFallBack() method invoked");
        return ResponseEntity.ok("Java 22");
    }

    @Operation(summary = "Get Maven Version")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Maven Version Successfully Retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping("/maven-version")
    public ResponseEntity<String> mavenVersion(){
        return ResponseEntity.ok(environment.getProperty("MAVEN_HOME"));
    }


    @Operation(summary = "Get Contact Info")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Contact Information Successfully Retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfo> getContactInfo(){
        return ResponseEntity.ok(accountsContactInfo);
    }

}
