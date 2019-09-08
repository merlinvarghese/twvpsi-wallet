package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="/wallets")
public class WalletController {

  @Autowired
  private WalletService walletService;

  @PostMapping
  @ResponseStatus (HttpStatus.CREATED)
   Wallet createWallet(@RequestBody Wallet wallet){
    return walletService.createWallet(wallet);

  }

  @GetMapping("/list")
  Wallet[] listWallets(){
    return walletService.listWallets();

  }


}
