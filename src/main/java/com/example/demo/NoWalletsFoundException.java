package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NoWalletsFoundException extends Exception {

  public NoWalletsFoundException(String message) {
    super(message);
  }
}
