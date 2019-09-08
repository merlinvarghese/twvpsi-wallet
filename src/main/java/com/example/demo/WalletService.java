package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class WalletService {

  private Map<String, Wallet> wallets = new HashMap<String, Wallet>();

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  Wallet createWallet(@RequestBody Wallet wallet){
    wallets.put(wallet.getAccountHolderName(), wallet);
    return wallet;
  }

  @GetMapping
  Wallet[] listWallets(){
    System.out.println("listWallets invoked!");
    Wallet[] walletArray = new Wallet[wallets.size()];
    wallets.values().toArray(walletArray);
    return walletArray;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  Wallet getWalletDetails(@RequestParam String name){
    return wallets.get(name);
  }


  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  String removeWallet(@RequestParam String name){
    System.out.println("Removing " + name);
    System.out.println(wallets.remove(name));
    System.out.println("Current Wallet HashMap " + wallets);
    return name;
  }

}
