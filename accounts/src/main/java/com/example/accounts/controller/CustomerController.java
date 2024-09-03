package com.example.accounts.controller;

import com.example.accounts.dto.AccountsContactInfo;
import com.example.accounts.dto.CustomerRequest;
import com.example.accounts.dto.CustomerResponse;
import com.example.accounts.dto.ErrorResponseDTO;
import com.example.accounts.service.CustomerService;
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



    @Operation(summary = "Get Build Information")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Build Information Successfully Retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping("/build-info")
    public ResponseEntity<String> buildInfo(){
        return ResponseEntity.ok(buildVersion);
    }

    @Operation(summary = "Get Java Version")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Java Version Successfully Retrieved"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping("/java-version")
    public ResponseEntity<String> javaVersion(){
        return ResponseEntity.ok(environment.getProperty("JAVA_HOME"));
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
