package com.phyllipesa.erudio.unittests.mocks;

import com.phyllipesa.erudio.data.vo.v1.BookVO;
import com.phyllipesa.erudio.models.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {

  public Book mockEntity() {
    return mockEntity(0);
  }

  public BookVO mockVO() {
    return mockVO(0);
  }

  public List<Book> mockEntityList() {
    List<Book> books = new ArrayList<Book>();
    for (int i = 0; i < 14; i++) {
      books.add(mockEntity(i));
    }
    return books;
  }

  public List<BookVO> mockVOList() {
    List<BookVO> books = new ArrayList<>();
    for (int i = 0; i < 14; i++) {
      books.add(mockVO(i));
    }
    return books;
  }

  public Book mockEntity(Integer number) {
    Book book = new Book();
    book.setId(number.longValue());
    book.setAuthor("some author Test" + number);
    book.setlaunchDate(new Date());
    book.setPrice(25D);
    book.setTitle("some title Test" + number);
    return book;
  }

  public BookVO mockVO(Integer number) {
    BookVO bookVO = new BookVO();
    bookVO.setKey(number.longValue());
    bookVO.setAuthor("some author Test" + number);
    bookVO.setlaunchDate(new Date());
    bookVO.setPrice(25D);
    bookVO.setTitle("some title Test" + number);
    return bookVO;
  }
}

