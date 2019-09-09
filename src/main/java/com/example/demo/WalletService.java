package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class WalletService {

  @Autowired
  WalletRepository walletRepository;

  Wallet createWallet(@RequestBody Wallet wallet){
    walletRepository.save(wallet);
    return wallet;
  }

  //Wallet
 /* Wallet[] listWallets(){
    walletRepository.findById()
    return walletArray;
  }*/

/*  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  Wallet getWalletDetails(@RequestParam String name){
    return wallets.get(name);
  }


  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  String removeWallet(@RequestParam String name) throws UserNotFoundException{

    if(wallets.containsKey(name)){
      wallets.remove(name);
      return name;
    }
    throw new UserNotFoundException();
  }*/

}
