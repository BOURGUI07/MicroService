package com.example.loans.controller;

import com.example.loans.dto.ErrorResponseDTO;
import com.example.loans.dto.LoanRequest;
import com.example.loans.dto.LoanResponse;
import com.example.loans.dto.LoansContactDetails;
import com.example.loans.service.LoanService;
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

import java.util.List;

@RestController
@RequestMapping(path="/api/loans",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Validated
@Tag(name="Loan", description=" Loan Controller")
public class LoanController {
    @NonFinal @Value("${build.version}")
    String buildVersion;
    LoanService loanService;
    Environment environment;
    LoansContactDetails loansContactDetails;
    @Operation(summary="Create a new  Loan")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="Loan is successfully created",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanResponse.class)) }),
            @ApiResponse(responseCode="400", description="Client Entered a non Valid Entity Body",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoanResponse> createLoan(
            @Valid @RequestBody LoanRequest loanRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.create(loanRequest));
    }



    @Operation(summary="Update loan")
    @ApiResponses(value={
            @ApiResponse(responseCode="404", description="loan isn't found",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode="200", description="loan was successfully Updated",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanResponse.class)) }),
            @ApiResponse(responseCode="400", description="Client Entered a Negative id Or "
                    + "a Non Valid Entity Body",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @PutMapping(value="/{loanId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoanResponse> updateLoan(
            @PathVariable Integer loanId,
            @RequestBody @Valid LoanRequest loanRequest) {

        return ResponseEntity.status(HttpStatus.OK).body(loanService.update(loanId,loanRequest));
    }


    @Operation(summary="Delete Loan By Id")
    @ApiResponses(value={
            @ApiResponse(responseCode="404", description="loan isn't found",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode="204", description="loan was successfully Deleted",
                    content = @Content),
            @ApiResponse(responseCode="400", description="Client Entered a non-valid phone number",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer loanId) {
        loanService.deleteById(loanId);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary="Get loan by phone", description="Retrieve a single customer by phone")
    @ApiResponses(value={
            @ApiResponse(responseCode="404", description="loan isn't found",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode="200", description="loan was successfully Found",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanResponse.class)) }),
            @ApiResponse(responseCode="400", description="Client Entered a Negative id",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<LoanResponse>> findByPhone(@RequestParam @Valid String phone){
        return ResponseEntity.ok(loanService.findByPhone(phone));
    }



    @Operation(summary="Get Build Version")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Build Version was Successfully Retrieved"),
            @ApiResponse(responseCode = "500",description = "Internal Sever Error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping("/build-info")
    public ResponseEntity<String> buildVersion(){
        return ResponseEntity.ok(buildVersion);
    }


    @Operation(summary="Get Java Version")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Java Version was Successfully Retrieved"),
            @ApiResponse(responseCode = "500",description = "Internal Sever Error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping("/java-version")
    public ResponseEntity<String> javaVersion(){
        return ResponseEntity.ok(environment.getProperty("JAVA_HOME"));
    }

    @Operation(summary="Get Loans Contact Details")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Contact Details were Successfully Retrieved",
                    content = @Content(schema=@Schema(implementation= LoansContactDetails.class))),
            @ApiResponse(responseCode = "500",description = "Internal Sever Error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping("/contact-info")
    public ResponseEntity<LoansContactDetails> contactInfo(){
        return ResponseEntity.ok(loansContactDetails);
    }
}
