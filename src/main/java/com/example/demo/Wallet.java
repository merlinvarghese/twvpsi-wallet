package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
class Wallet {

  @Id
  @GeneratedValue
  @JsonProperty
  private Long id;

  @JsonProperty
  //@NotEmpty(message = "Please provide a name")
  private String name;

  @JsonProperty
  //@NotEmpty(message = "Please provide a balance amount")
  private double balance;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "wallet")
  private List<Transaction> transactions;

  Wallet(String name, double balance) {
    this.name = name;
    this.balance = balance;
  }

  public Wallet(long id, String name, double balance) {
    this.id = id;
    this.name = name;
    this.balance = balance;
    this.transactions = new ArrayList<com.example.demo.Transaction>();
  }

  @JsonIgnore
  String getBalance() {
    return Double.toString(balance);
  }

  Wallet() {

  }

  @JsonIgnore
  public Long getId() {
    return id;
  }

  @JsonIgnore
  public Long getWalletId() {
    return id;
  }

  @JsonIgnore
  public String getName() {
    return name;
  }

  void process(com.example.demo.Transaction transaction) {
    balance += transaction.getConvertedAmount();
    transaction.bindWallet(this);
    transactions.add(transaction);
  }

  @JsonIgnore
  public List<com.example.demo.Transaction> getTransactions() {
    return transactions;
  }

  @JsonIgnore
  public int getTransactionsSize() {
    return transactions.size();
  }

  public boolean isNameEqual(String otherName) {
    return name.equals(otherName);
  }

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
    return Objects.hash(id);
  }

}
