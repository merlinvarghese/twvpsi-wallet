package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class WalletService {

  @Autowired
  WalletRepository walletRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  Wallet createWallet(Wallet wallet) {
    return walletRepository.save(wallet);
  }

  Wallet getWalletById(Long id) throws NoWalletsFoundException {
    return walletRepository.findById(id).orElseThrow(() -> new NoWalletsFoundException(""));
  }

  Wallet getWalletByName(String inputName) throws NoWalletsFoundException {
    Iterable<Wallet> wallets = walletRepository.findAll();
    for (Wallet wallet : wallets) {
      if (wallet.isNameEqual(inputName)) {
        return wallet;
      }
    }

    throw new NoWalletsFoundException("A Wallet does not exist for this user");
  }

  List<Wallet> getAllWallets() throws NoWalletsFoundException {
    List<Wallet> wallets = new LinkedList<>();
    Iterable<Wallet> fetchedWallets = walletRepository.findAll();

    for (Wallet wallet : fetchedWallets) {
      wallets.add(wallet);
    }

    if (wallets.size() > 0) {
      return wallets;
    }

    throw new NoWalletsFoundException("No Wallets found");
  }

  void deleteWallet(Long id) throws NoWalletsFoundException {
    try {
      getWalletById(id);
    } catch (NoWalletsFoundException walletNotFound) {
      throw walletNotFound;
    }

    walletRepository.deleteById(id);
  }

  public Transactions performTransaction(Transactions transaction, Long walletId)
      throws NoWalletsFoundException, InsufficientBalanceException {

    Wallet walletToUpdate = getWalletById(walletId);
    walletToUpdate.updateBalance(transaction);
    walletRepository.save(walletToUpdate);
    Wallet updatedWallet = getWalletById(walletId);
    return updatedWallet.getTransactions().get(updatedWallet.getTransactionsSize() - 1);
  }

  public List<Transactions> getAllTransactions(long walletId) throws NoWalletsFoundException {
    Wallet wallet = getWalletById(walletId);
    return wallet.getTransactions();
  }

}
