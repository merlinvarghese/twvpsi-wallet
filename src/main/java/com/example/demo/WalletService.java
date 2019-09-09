package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class WalletService {

  @Autowired
  WalletRepository walletRepository;

  Wallet createWallet(@RequestBody Wallet wallet) {
    walletRepository.save(wallet);
    return wallet;
  }


}
