package com.example.cards.controller;

import com.example.cards.dto.CardResponse;
import com.example.cards.dto.CardsContactDetails;
import com.example.cards.dto.ErrorResponseDTO;
import com.example.cards.dto.CardRequest;
import com.example.cards.service.CardService;
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
@RequestMapping(path="/api/cards",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Validated
@Tag(name="Card", description=" Card Controller")
public class CardsController {
    CardService service;
    Environment environment;
    CardsContactDetails cardsContactDetails;
    @Operation(summary="Create a new  Card")
    @ApiResponses(value={
            @ApiResponse(responseCode="201", description="card is successfully created",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardResponse.class)) }),
            @ApiResponse(responseCode="400", description="Client Entered a non Valid Entity Body",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardResponse> createCard(
            @Valid @RequestBody CardRequest cardRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(cardRequest));
    }



    @Operation(summary="Update card")
    @ApiResponses(value={
            @ApiResponse(responseCode="404", description="card isn't found",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode="200", description="card was successfully Updated",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardResponse.class)) }),
            @ApiResponse(responseCode="400", description="Client Entered a Negative id Or "
                    + "a Non Valid Entity Body",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @PutMapping(value="/{cardId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardResponse> updateCard(
            @PathVariable Integer cardId,
            @RequestBody @Valid CardRequest cardRequest) {

        return ResponseEntity.status(HttpStatus.OK).body(service.update(cardId,cardRequest));
    }


    @Operation(summary="Delete card By Id")
    @ApiResponses(value={
            @ApiResponse(responseCode="404", description="card isn't found",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode="204", description="card was successfully Deleted",
                    content = @Content),
            @ApiResponse(responseCode="400", description="Client Entered a non-valid phone number",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @DeleteMapping("/{loanId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer loanId) {
        service.deleteById(loanId);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary="Get card by phone", description="Retrieve a single customer by phone")
    @ApiResponses(value={
            @ApiResponse(responseCode="404", description="card isn't found",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode="200", description="card was successfully Found",content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardResponse.class)) }),
            @ApiResponse(responseCode="400", description="Client Entered a Negative id",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema=@Schema(implementation= ErrorResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<CardResponse>> findByPhone(@RequestParam @Valid String phone){
        return ResponseEntity.ok(service.findByPhone(phone));
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
    public ResponseEntity<CardsContactDetails> getContactInfo(){
        return ResponseEntity.ok(cardsContactDetails);
    }
}
