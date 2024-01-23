package com.phyllipesa.udm.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonPropertyOrder({ "id", "author", "lauchDate", "price", "title" })
public class BookVO extends RepresentationModel<BookVO> implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private Long key;
  private String author;
  private Date launchDate;
  private Double price;
  private String title;

  public BookVO() {
  }

  public BookVO(Long key, String author, Date launchDate, Double price, String title) {
    this.key = key;
    this.author = author;
    this.launchDate = launchDate;
    this.price = price;
    this.title = title;
  }

  public Long getKey() {
    return key;
  }

  public void setKey(Long key) {
    this.key = key;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Date getlaunchDate() {
    return launchDate;
  }

  public void setlaunchDate(Date lauchDate) {
    this.launchDate = lauchDate;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BookVO bookVO)) return false;
    return Objects.equals(key, bookVO.key) && Objects.equals(author, bookVO.author) && Objects.equals(launchDate, bookVO.launchDate) && Objects.equals(price, bookVO.price) && Objects.equals(title, bookVO.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, author, launchDate, price, title);
  }
}
