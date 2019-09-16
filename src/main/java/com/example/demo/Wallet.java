package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
class Wallet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty
  private Long id;

  @JsonProperty
  @NotEmpty(message = "Please provide a name")
  @Size(min = 3,max=12)
  private String name;

  @JsonProperty
  @DecimalMin("1.0")
  private double balance;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "wallet")
  @JsonProperty
  private List<Transactions> transactions;

  Wallet() {

  }

  Wallet(String name, double balance) {
    this.name = name;
    this.balance = balance;
  }

  public Wallet(long id, String name, double balance) {
    this.id = id;
    this.name = name;
    this.balance = balance;
    this.transactions = new ArrayList<Transactions>();
  }

  double getBalance() {
    return balance;
  }

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

  public List<Transactions> getTransactions() {
    return transactions;
  }

  @JsonIgnore
   int getTransactionsSize() {
    return transactions.size();
  }

  boolean isNameEqual(String otherName) {
    return name.equals(otherName);
  }

  void updateBalance(Transactions transaction) {
    balance += transaction.processedAmount();
    transaction.bindWallet(this);
    this.transactions.add(transaction);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Wallet wallet = (Wallet) o;
    return Objects.equals(id, wallet.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Wallet{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", balance=" + balance +
        ", transactions=" + transactions +
        '}';
  }
}
