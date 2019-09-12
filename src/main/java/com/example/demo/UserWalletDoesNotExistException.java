package com.example.demo;

public class UserWalletDoesNotExistException extends Exception {

  public UserWalletDoesNotExistException(String message){
    super(message);
  }
}
