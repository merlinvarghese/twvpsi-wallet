package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Transactions {

  enum TransactionType {
    DEBIT,
    CREDIT
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty
  private Long id;

  @JsonProperty
  private TransactionType transactionType;

  @JsonProperty
  private double amount;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "wallet_id")
  private Wallet wallet;

  public Transactions(TransactionType transactionType, double amount) {
    this.transactionType = transactionType;
    this.amount = amount;
  }

  public Transactions(Long id, TransactionType transactionType, double amount) {
    this.id = id;
    this.transactionType = transactionType;
    this.amount = amount;
  }

  Transactions() {
  }

  @JsonIgnore
  public Long getId() {
    return id;
  }

  @JsonIgnore
  // @NotEmpty(message = "Please provide a transaction type")
  public TransactionType getTransactionType() {
    return transactionType;
  }

  @JsonIgnore
  //@NotEmpty(message = "Please provide an amount")
  public double getAmount() {
    return amount;
  }

  @JsonIgnore
  public Wallet getWallet() {
    return wallet;
  }

  public void bindWallet(Wallet walletToUpdate) {
    this.wallet = walletToUpdate;
  }

  /*public double getConvertedAmount() {
    if (transactionType.equals(TransactionType.CREDIT)) {
      return amount;
    } else if (transactionType.equals(TransactionType.DEBIT)) {
      return amount * -1;
    }
    return 0;
  }*/

  double processedAmount() {
    return transactionType.equals(TransactionType.CREDIT) ? amount : (amount * -1);
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

