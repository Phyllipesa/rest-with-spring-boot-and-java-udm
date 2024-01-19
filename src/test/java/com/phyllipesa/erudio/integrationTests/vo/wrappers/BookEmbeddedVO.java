package com.phyllipesa.erudio.integrationTests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.phyllipesa.erudio.integrationTests.vo.BookVO;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class BookEmbeddedVO implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @JsonProperty("bookVOList")
  private List<BookVO> books;

  public BookEmbeddedVO() {}

  public List<BookVO> getBooks() {
    return books;
  }

  public void setBooks(List<BookVO> books) {
    this.books = books;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BookEmbeddedVO that)) return false;
    return Objects.equals(books, that.books);
  }

  @Override
  public int hashCode() {
    return Objects.hash(books);
  }
}
