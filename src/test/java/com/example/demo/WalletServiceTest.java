package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
  void expectAllWalletsReturnedWhenNoIDGiven() throws NoWalletsFoundException {
    Wallet firstWallet = new Wallet("George", 1000);
    Wallet secondWallet = new Wallet("Joseph", 1000);
    Wallet createdFirstWallet = walletService.createWallet(firstWallet);
    Wallet createdSecondWallet = walletService.createWallet(secondWallet);
    List<Wallet> expectedWallets = new LinkedList<>();
    expectedWallets.add(createdFirstWallet);
    expectedWallets.add(createdSecondWallet);

    List<Wallet> actualWallets = walletService.getAllWallets();

    assertEquals(expectedWallets, actualWallets);
  }

  @Test
  void expectNoWalletsFoundExceptionWhenTryingToFetchAllWallets() throws NoWalletsFoundException {
    assertThrows(NoWalletsFoundException.class, () -> walletService.getAllWallets());
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
  void expectWalletDeletedWithGivenUserId() throws NoWalletsFoundException {
    Wallet firstWallet = new Wallet("George", 1000);
    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);

    assertDoesNotThrow(() -> walletService.deleteWallet(createdGeorgeWallet.getId()));
  }

  @Test
  void expectCannotDeleteWalletWhenWalletWithGivenUserIdDoesNotExist() throws NoWalletsFoundException {
    assertThrows(NoWalletsFoundException.class, () -> walletService.deleteWallet((long) 100));
  }

  @Test
  void expectBalanceUpdatedWithCreditTransaction() throws NoWalletsFoundException,InsufficientBalanceException {
    Wallet firstWallet = new Wallet("George", 1000);
    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
    long createdWalletId = createdGeorgeWallet.getId();
    Transactions creditTransactions = new Transactions(Transactions.TransactionType.CREDIT, 100);

    Transactions createdTransactions = walletService.performTransaction(creditTransactions, createdWalletId);

    Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();
    assertTrue("1100.0".equals(fetchedWallet.getBalance()));
  }

  @Test
  void expectTransactionCreatedForWallet() throws NoWalletsFoundException,InsufficientBalanceException {
    Wallet firstWallet = new Wallet("George", 1000);
    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
    long createdWalletId = createdGeorgeWallet.getId();
    Transactions credit = new Transactions(Transactions.TransactionType.CREDIT, 100);

    walletService.performTransaction(credit, createdWalletId);

    Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();
    assertEquals(1, fetchedWallet.getTransactionsSize());
  }

  @Test
  void expectReferencesCreatedOfTransactionAndWalletOnEachOther() throws NoWalletsFoundException,InsufficientBalanceException {
    Wallet firstWallet = new Wallet("George", 1000);
    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
    long createdWalletId = createdGeorgeWallet.getId();
    Transactions credit = new Transactions(Transactions.TransactionType.CREDIT, 100);

    walletService.performTransaction(credit, createdWalletId);

    Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();
    Transactions transactionsOnWallet = getTransactionAssociatedWithWallet(fetchedWallet);
    Transactions savedTransactions = getLastSavedTransaction(transactionsOnWallet);
    assertEquals(fetchedWallet.getId(), transactionsOnWallet.getWallet().getId());
    assertNotNull(savedTransactions);
  }

  @Test
  void expectWalletAndItsTransactionsDeleted() throws NoWalletsFoundException,InsufficientBalanceException {
    Wallet firstWallet = new Wallet("George", 1000);
    Wallet createdWallet = walletService.createWallet(firstWallet);
    long createdWalletId = createdWallet.getId();
    Transactions credit = new Transactions(Transactions.TransactionType.CREDIT, 100);
    createdWallet = walletRepository.findById(createdWalletId).get();
    walletService.performTransaction(credit, createdWalletId);

    walletService.deleteWallet(createdWalletId);

    Optional<Wallet> fetchedWallet = walletRepository.findById(createdWalletId);
    int count = getSavedTransactionsCount();
    assertFalse(fetchedWallet.isPresent());
    assertTrue(count == 0);
  }

  /*Write tests for fetch all transactions - 3 cases */
  /*Write tests for validations" */

  private int getSavedTransactionsCount() {
    Iterable<Transactions> allTransactions = transactionRepository.findAll();
    int size = 0;
    if (allTransactions instanceof Collection<?>) {
      size = ((Collection<?>) allTransactions).size();
    }
    return size;
  }

  private Transactions getTransactionAssociatedWithWallet(Wallet fetchedWallet) {
    return fetchedWallet.getTransactions().get(0);
  }

  private Transactions getLastSavedTransaction(Transactions transactionsOnWallet) {
    return transactionRepository.findById(transactionsOnWallet.getId()).get();
  }
}
