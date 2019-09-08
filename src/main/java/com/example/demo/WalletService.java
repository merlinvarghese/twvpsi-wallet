package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class WalletService {

  private List<Wallet> wallets = new ArrayList<>();

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Wallet createWallet(@RequestBody Wallet wallet){
    wallets.add(wallet);
    return wallet;

  }

  @GetMapping
  Wallet[] listWallets(){
    System.out.println("listWallets invoked!");
    Wallet[] walletArray = new Wallet[wallets.size()];
    wallets.toArray(walletArray);
    return walletArray;
  }
}
