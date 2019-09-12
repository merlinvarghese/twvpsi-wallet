package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(value ="/wallets")
public class WalletController {

  @Autowired
  private WalletService walletService;

 /* @PostMapping
  @ResponseStatus (HttpStatus.CREATED)
   Wallet createWallet(@RequestBody Wallet wallet){
    return walletService.createWallet(wallet);

  }

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  Wallet findWalletById(@RequestParam("id") Long id){
    walletService.findWalletById(id);
    return walletService.findWalletById(id).get();
  }
  */

  @PostMapping("")
  ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet) {
    Wallet respWallet = walletService.createWallet(wallet);
    return new ResponseEntity<>(respWallet, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  ResponseEntity<Wallet> getWalletById(@PathVariable("id") Long id) {
    try {
      Wallet respWallet = walletService.getWalletById(id);
      return new ResponseEntity<>(respWallet, HttpStatus.OK);
    } catch (UserWalletDoesNotExistException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("")
  ResponseEntity<List<Wallet>> getWallet(@RequestParam(value = "name", defaultValue = "all") String name) {
    if ("all".equals(name)) {
      return getAllWallets();
    }

    return getWalletByName(name);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<HttpStatus> deleteWallet(@PathVariable("id") Long id) {
    try {
      walletService.deleteWallet(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (UserWalletDoesNotExistException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping("/{id}/transactions")
  ResponseEntity<Transaction> performTransaction(@PathVariable("id") Long walletId, @RequestBody Transaction transaction)
  {
    try {
      Transaction createdTransaction = walletService.performTransaction(transaction, walletId);
      return new ResponseEntity<Transaction>(createdTransaction, HttpStatus.CREATED);
    } catch (UserWalletDoesNotExistException e) {
      return new ResponseEntity<Transaction>(HttpStatus.NOT_FOUND);
    }
  }

  private ResponseEntity<List<Wallet>> getWalletByName(String name) {
    try {
      List<Wallet> wallets = new LinkedList<>();
      Wallet respWallet = walletService.getWalletByName(name);
      wallets.add(respWallet);
      return new ResponseEntity<>(wallets, HttpStatus.OK);
    } catch (UserWalletDoesNotExistException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  private ResponseEntity<List<Wallet>> getAllWallets() {
    List<Wallet> wallets;
    try {
      wallets = walletService.getAllWallets();
      return new ResponseEntity<>(wallets, HttpStatus.OK);
    } catch (NoWalletsFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
