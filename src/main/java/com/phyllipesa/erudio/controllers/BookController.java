package com.phyllipesa.erudio.controllers;

import com.phyllipesa.erudio.data.vo.v1.BookVO;
import com.phyllipesa.erudio.services.BookService;
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
@RequestMapping("/api/book/v1")
public class BookController {

  @Autowired
  BookService service;

  @GetMapping(
      produces = {
          MediaType.APPLICATION_JSON,
          MediaType.APPLICATION_XML,
          MediaType.APPLICATION_YML
      }
  )
  @Operation(
      summary = "Finds all books",
      description = "Finds all books",
      tags = {"Books"},
      responses = {
          @ApiResponse(
              description = "Success",
              responseCode = "200",
              content = {
                  @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BookVO.class)))
              }
          ),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
          @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
      }
  )
  public ResponseEntity<List<BookVO>> findAll() {
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
      summary = "Find a book",
      description = "Find a book",
      tags = {"Books"},
      responses = {
          @ApiResponse(description = "Success", responseCode = "200",
              content = @Content(schema = @Schema(implementation = BookVO.class))
          ),
          @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
          @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
      }
  )
  public ResponseEntity<BookVO> findById(@PathVariable(value = "id") Long id) {
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
      summary = "Add a new book",
      description = "Add a new book by passing in a JSOn, XML or YML representation of the book!",
      tags = {"Books"},
      responses = {
          @ApiResponse(description = "Success", responseCode = "200",
              content = @Content(schema = @Schema(implementation = BookVO.class))
          ),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
      }
  )
  public ResponseEntity<BookVO> create(@RequestBody BookVO book) {
    return ResponseEntity.status(201).body(service.create(book));
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
      summary = "Updates a book",
      description = "Updates a book by passing in a JSOn, XML or YML representation of the book!",
      tags = {"Books"},
      responses = {
          @ApiResponse(description = "Success", responseCode = "200",
              content = @Content(schema = @Schema(implementation = BookVO.class))
          ),
          @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
          @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
          @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
          @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
      }
  )
  public ResponseEntity<BookVO> update(@RequestBody BookVO book) {
    return ResponseEntity.status(200).body(service.update(book));
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Deletes a book",
      description = "Deletes a book referring to the provided id!",
      tags = {"Books"},
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
