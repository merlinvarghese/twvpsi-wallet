package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Transaction {
  @Id
  @GeneratedValue
  @JsonProperty
  private Long id;

  @JsonProperty
  private TransactionType transactionType;

  @JsonProperty
  private double amount;

  enum TransactionType {
    DEBIT,
    CREDIT
  }

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name="wallet_id")
  private Wallet wallet;

  public Transaction(TransactionType transactionType, double amount) {
    this.transactionType = transactionType;
    this.amount = amount;
  }

  public Transaction() {
  }

  @JsonIgnore
  public Long getId() {
    return id;
  }

  @JsonIgnore
  public TransactionType getTransactionType() {
    return transactionType;
  }

  @JsonIgnore
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

  public double getConvertedAmount() {
    if (transactionType.equals(TransactionType.CREDIT)) {
      return amount;
    }
    else if (transactionType.equals(TransactionType.DEBIT)) {
      return amount * -1;
    }

    return 0;
  }
}

