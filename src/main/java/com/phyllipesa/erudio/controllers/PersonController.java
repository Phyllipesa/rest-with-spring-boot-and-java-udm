package com.phyllipesa.erudio.controllers;

import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.services.PersonService;
import com.phyllipesa.erudio.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

  @Autowired
  PersonService service;

  @GetMapping(
    produces = {
      MediaType.APPLICATION_JSON,
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YML
    }
  )
  @Operation(
    summary = "Finds all people",
    description = "Finds all people",
    tags = {"People"},
    responses = {
      @ApiResponse(
        description = "Success",
        responseCode = "200",
        content = {
          @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonVO.class)))
        }
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public ResponseEntity<List<PersonVO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping(
    value = "/{id}",
    produces = {
      MediaType.APPLICATION_JSON,
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YML
    }
  )
  @Operation(
    summary = "Find a person",
    description = "Find a person",
    tags = {"People"},
    responses = {
      @ApiResponse(description = "Success", responseCode = "200",
        content = @Content(schema = @Schema(implementation = PersonVO.class))
      ),
      @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public ResponseEntity<PersonVO> findById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping(
    consumes = {
      MediaType.APPLICATION_JSON,
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YML
    },
    produces = {
      MediaType.APPLICATION_JSON,
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YML
    }
  )
  @Operation(
    summary = "Add a new person",
    description = "Add a new person by passing in a JSOn, XML or YML representation of the person!",
    tags = {"People"},
    responses = {
      @ApiResponse(description = "Success", responseCode = "200",
        content = @Content(schema = @Schema(implementation = PersonVO.class))
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public ResponseEntity<PersonVO> create(@RequestBody PersonVO person) {
    return ResponseEntity.status(201).body(service.create(person));
  }

  @PutMapping(
    consumes = {
      MediaType.APPLICATION_JSON,
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YML
    },
    produces = {
      MediaType.APPLICATION_JSON,
      MediaType.APPLICATION_XML,
      MediaType.APPLICATION_YML
    }
  )
  @Operation(
    summary = "Updates a person",
    description = "Updates a person by passing in a JSOn, XML or YML representation of the person!",
    tags = {"People"},
    responses = {
      @ApiResponse(description = "Success", responseCode = "200",
        content = @Content(schema = @Schema(implementation = PersonVO.class))
      ),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public ResponseEntity<PersonVO> update(@RequestBody PersonVO person) {
    return ResponseEntity.status(200).body(service.update(person));
  }

  @DeleteMapping("/{id}")
  @Operation(
    summary = "Deletes a person",
    description = "Deletes a person referring to the provided id!",
    tags = {"People"},
    responses = {
      @ApiResponse(description = "No content", responseCode = "204", content = @Content),
      @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
      @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
      @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
      @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    }
  )
  public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
