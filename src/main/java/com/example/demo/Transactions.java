package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Transactions {

  enum TYPE {
    DEBIT,
    CREDIT
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty
  private Long id;

  @JsonProperty
  private TYPE type;

  @JsonProperty
  private double amount;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "wallet_id")
  private Wallet wallet;

  public Transactions(TYPE type, double amount) {
    this.type = type;
    this.amount = amount;
  }

  public Transactions(Long id, TYPE type, double amount) {
    this.id = id;
    this.type = type;
    this.amount = amount;
  }

  Transactions() {
  }

  @JsonIgnore
  public Long getId() {
    return id;
  }

  @JsonIgnore
  public TYPE getType() {
    return type;
  }

  @JsonIgnore
  public double getAmount() {
    return amount;
  }

  @JsonIgnore
  public Wallet getWallet() {
    return wallet;
  }

  void bindWallet(Wallet walletToUpdate) {
    this.wallet = walletToUpdate;
  }

  double processedAmount() {
    return type.equals(TYPE.CREDIT) ? amount : (amount * -1);
  }

  @Override
   public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Transactions that = (Transactions) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

