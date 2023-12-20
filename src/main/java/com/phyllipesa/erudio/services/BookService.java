package com.phyllipesa.erudio.services;

import com.phyllipesa.erudio.controllers.BookController;
import com.phyllipesa.erudio.data.vo.v1.BookVO;
import com.phyllipesa.erudio.exceptions.RequiredObjectIsNullException;
import com.phyllipesa.erudio.exceptions.ResourceNotFoundException;
import com.phyllipesa.erudio.mapper.EntityMapper;
import com.phyllipesa.erudio.models.Book;
import com.phyllipesa.erudio.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
  EntityMapper entityMapper;

  public List<BookVO> findAll() {
    logger.info("Finding all books!");
    List<BookVO> list = entityMapper.parseListObject(bookRepository.findAll(), BookVO.class);
    list.forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
    return list;
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

