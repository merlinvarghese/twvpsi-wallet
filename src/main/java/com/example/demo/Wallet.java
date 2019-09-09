package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
class Wallet {

  @Id
  @GeneratedValue
  private Long id;

  @JsonProperty
  private String name;

  @JsonProperty
  private double balance;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Wallet wallet = (Wallet) o;
    return Double.compare(wallet.balance, balance) == 0 &&
        Objects.equals(id, wallet.id) &&
        Objects.equals(name, wallet.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, balance);
  }

  public Wallet() {
  }

  public Wallet(Long id) {
    this.id = id;
  }

  Wallet(String name, double balance) {
    this.name = name;
    this.balance = balance;
  }

  String getBalance() {
    return name;
  }

  public Long getId() {
    return id;
  }
}