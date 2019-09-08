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

  @PostMapping("/getWalletDetails")
  @ResponseStatus (HttpStatus.OK)
  Wallet getWalletDetails(@RequestParam("name") String name){
    return walletService.getWalletDetails(name);
  }


  @PostMapping("/delete")
  @ResponseStatus (HttpStatus.OK)
  String removeWallet(@RequestParam("name") String name){
    return walletService.removeWallet(name);
  }
}
