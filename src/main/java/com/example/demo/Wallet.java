package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

class Wallet {
  @JsonProperty
  private String name;
  @JsonProperty
  private double balance;

  Wallet(String name, double balance) {
    this.name = name;
    this.balance = balance;
  }

  String getAccountHolderName() {
    return name;
  }
}