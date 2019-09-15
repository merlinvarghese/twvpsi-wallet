package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transactions, Long> {
}
