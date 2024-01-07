package com.phyllipesa.erudio.models;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 80)
  private String author;

  @Column(name = "launch_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date launchDate;

  @Column(nullable = false)
  private Double price;

  @Column(nullable = false, length = 250)
  private String title;

  public Book() {
  }

  public Book(Long id, String author, Date launchDate, Double price, String title) {
    this.id = id;
    this.author = author;
    this.launchDate = launchDate;
    this.price = price;
    this.title = title;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
    if (!(o instanceof Book book)) return false;
    return Objects.equals(id, book.id) && Objects.equals(author, book.author) && Objects.equals(launchDate, book.launchDate) && Objects.equals(price, book.price) && Objects.equals(title, book.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, author, launchDate, price, title);
  }
}
