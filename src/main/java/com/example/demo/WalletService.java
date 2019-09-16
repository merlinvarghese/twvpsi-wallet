package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

  @Autowired
  WalletRepository walletRepository;

  Wallet createWallet(Wallet wallet) {
    return walletRepository.save(wallet);
  }

  Wallet getWalletById(Long id) throws NoWalletsFoundException {
    return walletRepository.findById(id).orElseThrow(() -> new NoWalletsFoundException("No wallet found for this user"));
  }

  Wallet getWalletByName(String inputName) throws NoWalletsFoundException {
    Iterable<Wallet> wallets = walletRepository.findAll();
    for (Wallet wallet : wallets) {
      if (wallet.isNameEqual(inputName)) {
        return wallet;
      }
    }

    throw new NoWalletsFoundException("No wallet found for this user");
  }

  List<Wallet> getAllWallets() {
    return (List<Wallet>) walletRepository.findAll();
  }

  void deleteWallet(Long id) throws NoWalletsFoundException {
    try {
      walletRepository.deleteById(id);
    } catch (Exception e) {
      throw new NoWalletsFoundException("No wallet found for this user");
    }
    //walletRepository.deleteById(id).orElseThrow(NoWalletsFoundException::new);
  }

  public Transactions performTransaction(Transactions transaction, Long walletId)
      throws NoWalletsFoundException, InsufficientBalanceException {

    Wallet walletToUpdate = getWalletById(walletId);
    walletToUpdate.updateBalance(transaction);
    walletRepository.save(walletToUpdate);
    Wallet updatedWallet = getWalletById(walletId);
    List<Transactions> transactions = updatedWallet.getTransactions();
    return transactions.get(updatedWallet.getTransactionsSize() - 1);
  }

  public List<Transactions> getAllTransactions(long walletId) throws NoWalletsFoundException {
    Wallet wallet = getWalletById(walletId);
    return wallet.getTransactions();
  }

}
