package com.phyllipesa.erudio.services;

import com.phyllipesa.erudio.controllers.BookController;
import com.phyllipesa.erudio.data.vo.v1.BookVO;
import com.phyllipesa.erudio.data.vo.v1.PersonVO;
import com.phyllipesa.erudio.exceptions.RequiredObjectIsNullException;
import com.phyllipesa.erudio.exceptions.ResourceNotFoundException;
import com.phyllipesa.erudio.mapper.EntityMapper;
import com.phyllipesa.erudio.models.Book;
import com.phyllipesa.erudio.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

  private Logger logger = Logger.getLogger(BookService.class.getName());

  @Autowired
  BookRepository bookRepository;

  @Autowired
  PagedResourcesAssembler<BookVO> assembler;

  @Autowired
  EntityMapper entityMapper;

  public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
    logger.info("Finding all books!");

    var bookPage = bookRepository.findAll(pageable);
    var bookVosPage = bookPage.map( b -> entityMapper.parseObject(b, BookVO.class));
    bookVosPage.map(
        b -> b.add(
            linkTo(methodOn(BookController.class)
                .findById(b.getKey())).withSelfRel()));

    Link link = linkTo(
        methodOn(BookController.class)
            .findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "asc"
            )).withSelfRel();

    return assembler.toModel(bookVosPage, link);
  }

  public BookVO findById(Long id) {
    logger.info("Finding a book!");
    Book book = bookRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No record found for this ID!"));
    BookVO bookVO = entityMapper.parseObject(book, BookVO.class);
    bookVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
    return bookVO;
  }

  public BookVO create(BookVO bookVO) {
    if (bookVO == null) throw new RequiredObjectIsNullException();
    logger.info("Creating one book!");
    Book book = entityMapper.parseObject(bookVO, Book.class);
    BookVO newBook = entityMapper.parseObject(bookRepository.save(book), BookVO.class);
    newBook.add(linkTo(methodOn(BookController.class).findById(newBook.getKey())).withSelfRel());
    return  newBook;
  }

  public BookVO update(BookVO bookVO) {
    if (bookVO == null) throw new RequiredObjectIsNullException();
    logger.info("Update one book!");

    Book entity = bookRepository.findById(bookVO.getKey())
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

    entity.setAuthor(bookVO.getAuthor());
    entity.setlaunchDate(bookVO.getlaunchDate());
    entity.setPrice(bookVO.getPrice());
    entity.setTitle(bookVO.getTitle());

    BookVO vo = entityMapper.parseObject(bookRepository.save(entity), BookVO.class);
    vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
    return  vo;
  }

  public void delete(Long id) {
    logger.info("Deleting a book!");
    Book entity = bookRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));;
    bookRepository.delete(entity);
  }
}

