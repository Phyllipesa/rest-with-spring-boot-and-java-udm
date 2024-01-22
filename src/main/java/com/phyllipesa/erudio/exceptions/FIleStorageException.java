package com.phyllipesa.erudio.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FIleStorageException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public FIleStorageException(String e) {
    super(e);
  }

  public FIleStorageException(String e, Throwable cause) {
    super(e, cause);
  }
}
