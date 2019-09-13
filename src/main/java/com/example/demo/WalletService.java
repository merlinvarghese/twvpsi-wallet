package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
public class WalletService {

  @Autowired
  WalletRepository walletRepository;

  @Autowired
  private TransactionRepository transactionRepository;

  Wallet createWallet(Wallet wallet) {
    return walletRepository.save(wallet);
  }

  Wallet getWalletById(Long id) throws UserWalletDoesNotExistException {
    Optional<Wallet> wallet = walletRepository.findById(id);
    if (wallet.isPresent()) {
      return wallet.get();
    }

    throw new UserWalletDoesNotExistException("A Wallet does not exist for this user");
  }

  Wallet getWalletByName(String inputName) throws UserWalletDoesNotExistException {
    Iterable<Wallet> wallets = walletRepository.findAll();
    for (Wallet wallet : wallets) {
      if (wallet.isNameEqual(inputName)) {
        return wallet;
      }
    }

    throw new UserWalletDoesNotExistException("A Wallet does not exist for this user");
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

  void deleteWallet(Long id) throws UserWalletDoesNotExistException {
    try {
      getWalletById(id);
    } catch (UserWalletDoesNotExistException walletNotFound) {
      throw walletNotFound;
    }

    walletRepository.deleteById(id);
  }

  public Transaction createTransaction(Transaction transaction, Long walletId) throws UserWalletDoesNotExistException {
    Wallet walletToUpdate = getWalletById(walletId);
    walletToUpdate.process(transaction);
    walletRepository.save(walletToUpdate);
    Wallet updatedWallet = getWalletById(walletId);
    return updatedWallet.getTransactions().get(updatedWallet.getTransactionsSize() - 1);
  }

  public List<Transaction> getTransactions(Wallet walletId) {

    List transactions = new ArrayList();
    return transactions;
  }
}
