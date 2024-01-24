package com.phyllipesa.udm.controllers;

import com.phyllipesa.udm.data.vo.v1.BookVO;
import com.phyllipesa.udm.services.BookService;
import com.phyllipesa.udm.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
  public ResponseEntity<PagedModel<EntityModel<BookVO>>> findAll(
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "3") Integer size,
      @RequestParam(value = "direction", defaultValue = "asc") String direction
  ) {
    var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "author"));
    return ResponseEntity.ok(service.findAll(pageable));
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
