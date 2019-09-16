package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WalletServiceTest {

  @Autowired
  private WalletRepository walletRepository;

  @Autowired
  private WalletService walletService;

  @Autowired
  private TransactionRepository transactionRepository;

  @BeforeEach
  void setup() {
    walletRepository.deleteAll();
    transactionRepository.deleteAll();
  }

  @Test
  void expectWalletCreatedForAUser() {
    Wallet firstWallet = new Wallet("George", 1000);

    Wallet createdWallet = walletService.createWallet(firstWallet);

    Wallet fetchedWallet = walletRepository.findById(createdWallet.getId()).get();

    assertNotNull(fetchedWallet);
    assertEquals(firstWallet.getName(), fetchedWallet.getName());
    assertEquals(firstWallet.getBalance(), fetchedWallet.getBalance());
  }

  @Test
  void expectAllWalletsReturnedWhenNoIDGiven() {
    Wallet walletOne = new Wallet("George", 1000);
    Wallet walletOther = new Wallet("Joseph", 1000);
    Wallet createdWalletOne = walletService.createWallet(walletOne);
    Wallet createdWalletOther = walletService.createWallet(walletOther);
    List<Wallet> expectedWallets = new LinkedList<>();
    expectedWallets.add(createdWalletOne);
    expectedWallets.add(createdWalletOther);

    List<Wallet> actualWallets = walletService.getAllWallets();

    assertEquals(expectedWallets, actualWallets);
  }

  @Test
  void expectWalletReturnedForAGivenId() {
    Wallet firstWallet = new Wallet("George", 1000);

    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);

    assertDoesNotThrow(() -> walletService.getWalletById(createdGeorgeWallet.getId()));
  }

  @Test
  void expectNoWalletsFoundWhenWalletWithGivenIdDoesNotExist() {
    assertThrows(NoWalletsFoundException.class, () -> walletService.getWalletById((long) 1));
  }

  @Test
  void expectWalletWhenFetchedUsingName() {
    Wallet firstWallet = new Wallet("George", 1000);

    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);

    assertDoesNotThrow(() -> walletService.getWalletByName(createdGeorgeWallet.getName()));
  }

  @Test
  void expectNoWalletsFoundWhenWalletWithUsernameDoesNotExist() {
    Wallet firstWallet = new Wallet("George", 1000);

    walletService.createWallet(firstWallet);

    assertThrows(NoWalletsFoundException.class, () -> walletService.getWalletByName("Joseph"));
  }

  @Test
  void expectWalletDeletedWithGivenUserId() {
    Wallet firstWallet = new Wallet("George", 1000);
    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);

    assertDoesNotThrow(() -> walletService.deleteWallet(createdGeorgeWallet.getId()));
  }

  @Test
  void expectCannotDeleteWalletWhenWalletWithGivenUserIdDoesNotExist() {
    assertThrows(NoWalletsFoundException.class, () -> walletService.deleteWallet((long) 100));
  }

  @Test
  void expectBalanceUpdatedForCreditTransaction() throws NoWalletsFoundException, InsufficientBalanceException {
    Wallet firstWallet = new Wallet("George", 200);

    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
    long createdWalletId = createdGeorgeWallet.getId();
    Transactions creditTransactions = new Transactions(Transactions.TYPE.CREDIT, 100);
    Transactions createdTransactions = walletService.performTransaction(creditTransactions, createdWalletId);
    Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();

    assertEquals(300.0, fetchedWallet.getBalance());
  }

  @Test
  void expectTransactionCreatedForWallet() throws NoWalletsFoundException, InsufficientBalanceException {
    Wallet firstWallet = new Wallet("George", 1000);

    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
    long createdWalletId = createdGeorgeWallet.getId();
    Transactions credit = new Transactions(Transactions.TYPE.CREDIT, 100);
    walletService.performTransaction(credit, createdWalletId);
    Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();

    assertEquals(1, fetchedWallet.getTransactionsSize());
  }

  @Test
  void expectTransactionCreatedIsLinkedToAWallet() throws NoWalletsFoundException, InsufficientBalanceException {
    Wallet firstWallet = new Wallet("George", 1000);
    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
    long createdWalletId = createdGeorgeWallet.getId();
    Transactions credit = new Transactions(Transactions.TYPE.CREDIT, 100);

    walletService.performTransaction(credit, createdWalletId);

    Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();
    Transactions transactionsOnWallet = getTransactionAssociatedWithWallet(fetchedWallet);
    Transactions savedTransactions = getLastSavedTransaction(transactionsOnWallet);
    assertEquals(fetchedWallet.getId(), transactionsOnWallet.getWallet().getId());
    assertNotNull(savedTransactions);
  }

  private Transactions getTransactionAssociatedWithWallet(Wallet fetchedWallet) {
    return fetchedWallet.getTransactions().get(0);
  }

  private Transactions getLastSavedTransaction(Transactions transactionsOnWallet) {
    return transactionRepository.findById(transactionsOnWallet.getId()).get();
  }
}
