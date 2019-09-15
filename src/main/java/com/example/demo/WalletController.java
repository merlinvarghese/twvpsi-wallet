package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/wallets")
@Validated
public class WalletController {

  @Autowired
  private WalletService walletService;

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  Wallet createWallet(@Valid @RequestBody Wallet wallet) {
    Wallet createdWallet = walletService.createWallet(wallet);
    return createdWallet;
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  Wallet getWalletById(@PathVariable("id") Long id) throws NoWalletsFoundException {
    Wallet wallet = walletService.getWalletById(id);
    return wallet;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  void deleteWallet(@PathVariable("id") Long id) throws NoWalletsFoundException {
    walletService.deleteWallet(id);
  }

  @PostMapping("/{id}/transactions")
  @ResponseStatus(HttpStatus.CREATED)
  Transactions performTransaction(@PathVariable("id") Long walletId,
                                  @RequestBody Transactions transaction) throws NoWalletsFoundException, InsufficientBalanceException {
    return walletService.performTransaction(transaction, walletId);
  }

  @GetMapping("/{id}/transactions")
  @ResponseStatus(HttpStatus.OK)
  List<Transactions> getAllTransactions(@PathVariable(value = "id") Long id) throws NoWalletsFoundException {
    return walletService.getAllTransactions(id);

  }

  @RequestMapping
  List<Wallet> getWallets() throws NoWalletsFoundException {
    return walletService.getAllWallets();
  }

  /*@GetMapping("")
  List<Wallet> getWallet(@RequestParam(value = "name", defaultValue = "all") String name) throws NoWalletsFoundException {
    if ("all".equals(name)) {
      return getAllWallets();
    }

    return getWalletByName(name);
  }

  private Wallet getWalletByName(String name) throws NoWalletsFoundException {
      return walletService.getWalletByName(name);
  }
*/

  Wallet getWalletByName(@RequestParam(value = "name") String name) throws NoWalletsFoundException {
    return walletService.getWalletByName(name);
  }
/*
  @GetMapping("")
  List<Wallet> getWallet(@RequestParam(value = "name", defaultValue = "all") String name) throws NoWalletsFoundException {
    if ("all".equals(name)) {
      return getAllWallets();
    }
    return getWalletByName(name);
  }

  private List<Wallet> getAllWallets() throws NoWalletsFoundException {
    return walletService.getAllWallets();
  }*/

}

