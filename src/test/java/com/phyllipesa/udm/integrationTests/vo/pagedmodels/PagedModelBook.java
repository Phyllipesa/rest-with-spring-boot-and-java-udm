package com.phyllipesa.udm.integrationTests.vo.pagedmodels;

import com.phyllipesa.udm.integrationTests.vo.BookVO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@XmlRootElement
public class PagedModelBook implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @XmlElement(name = "content")
  private List<BookVO> content;

  public PagedModelBook() {}

  public List<BookVO> getContent() {
    return content;
  }

  public void setContent(List<BookVO> content) {
    this.content = content;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PagedModelBook that)) return false;
    return Objects.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }
}
