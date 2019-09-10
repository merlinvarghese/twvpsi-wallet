package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;


@Service
public class WalletService {

  @Autowired
  WalletRepository walletRepository;

  Wallet createWallet(@RequestBody Wallet wallet) {
    walletRepository.save(wallet);
    return wallet;
  }

  public Optional<Wallet> findWalletById(Long id) {
    return walletRepository.findById(id);

  }

  }
