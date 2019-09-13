package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
  TransactionRepository transactionRepository;

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
  void shouldReturnAllWallets() throws NoWalletsFoundException {
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
  void shouldFailWhenNoWalletsExists() throws NoWalletsFoundException {
    assertThrows(NoWalletsFoundException.class, () -> walletService.getAllWallets());
  }

  @Test
  void shouldReturnWalletWithGivenId() {
    Wallet firstWallet = new Wallet( "George", 1000);

    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);

    assertDoesNotThrow(() -> walletService.getWalletById(createdGeorgeWallet.getId()));
  }

  @Test
  void shouldFailWhenWalletWithGivenIdDoesNotExist() {
    assertThrows(UserWalletDoesNotExistException.class, () -> walletService.getWalletById((long) 1));
  }

  @Test
  void shouldReturnWalletWithGivenUserName() {
    Wallet firstWallet = new Wallet("George", 1000);

    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);

    assertDoesNotThrow(() -> walletService.getWalletByName(createdGeorgeWallet.getName()));
  }

  @Test
  void shouldFailWhenWalletWithGivenUsernameDoesNotExist() {
    Wallet firstWallet = new Wallet("George", 1000);

    walletService.createWallet(firstWallet);

    assertThrows(UserWalletDoesNotExistException.class, () -> walletService.getWalletByName("Joseph"));
  }

  @Test
  void expectWalletDeletedWithGivenUserId() throws UserWalletDoesNotExistException {
    Wallet firstWallet = new Wallet( "George", 1000);
    Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);

    assertDoesNotThrow(() -> walletService.deleteWallet(createdGeorgeWallet.getId()));
  }

  @Test
  void expectCannotDeleteWalletWhenWalletWithGivenUserIdDoesNotExist() throws UserWalletDoesNotExistException {
    assertThrows(UserWalletDoesNotExistException.class, () -> walletService.deleteWallet((long) 100));
  }

}
  /*}
/*

  @Nested
  class TransactionTests {
    @Test
    void shouldUpdateBalanceWithCreditTransaction() throws UserWalletDoesNotExistException {
      Wallet firstWallet = new Wallet("George", 1000);
      Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
      long createdWalletId = createdGeorgeWallet.getId();
      Transaction creditTransaction = new Transaction(Transaction.TransactionType.CREDIT, 100);

      Transaction createdTransaction = walletService.createTransaction(creditTransaction, createdWalletId);

      Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();
      assertTrue("1100.0".equals(fetchedWallet.getBalance()));
    }

    @Test
    void shouldCreateTransactionForWallet() throws UserWalletDoesNotExistException {
      Wallet firstWallet = new Wallet("George", 1000);
      Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
      long createdWalletId = createdGeorgeWallet.getId();
      Transaction credit = new Transaction(Transaction.TransactionType.CREDIT, 100);

      walletService.createTransaction(credit, createdWalletId);

      Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();
      assertEquals(1, fetchedWallet.getTransactionsSize());
    }

    @Test
    void shouldCreateReferencesOfTransactionAndWalletOnEachOther() throws UserWalletDoesNotExistException {
      Wallet firstWallet = new Wallet("George", 1000);
      Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
      long createdWalletId = createdGeorgeWallet.getId();
      Transaction credit = new Transaction(Transaction.TransactionType.CREDIT, 100);

      walletService.createTransaction(credit, createdWalletId);

      Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();
      Transaction transactionOnWallet = getTransactionAssociatedWithWallet(fetchedWallet);
      Transaction savedTransaction = getLastSavedTransaction(transactionOnWallet);
      assertEquals(fetchedWallet.getId(), transactionOnWallet.getWallet().getId());
      assertNotNull(savedTransaction);
    }

    @Test
    void shouldDeleteWalletAndItsTransactions() throws UserWalletDoesNotExistException {
      Wallet firstWallet = new Wallet( "George", 1000);
      Wallet createdWallet = walletService.createWallet(firstWallet);
      long createdWalletId = createdWallet.getId();
      Transaction credit = new Transaction(Transaction.TransactionType.CREDIT, 100);
      createdWallet = walletRepository.findById(createdWalletId).get();
      walletService.createTransaction(credit, createdWalletId);

      walletService.deleteWallet(createdWalletId);

      Optional<Wallet> fetchedWallet = walletRepository.findById(createdWalletId);
      int count = getSavedTransactionsCount();
      assertFalse(fetchedWallet.isPresent());
      assertTrue(count == 0);
    }


    private int getSavedTransactionsCount() {
      Iterable<Transaction> allTransactions = transactionRepository.findAll();
      int size = 0;
      if (allTransactions instanceof Collection<?>) {
        size = ((Collection<?>) allTransactions).size();
      }

      return size;
    }

    private Transaction getTransactionAssociatedWithWallet(Wallet fetchedWallet) {
      return fetchedWallet.getTransactions().get(0);
    }

    private Transaction getLastSavedTransaction(Transaction transactionOnWallet) {
      return transactionRepository.findById(transactionOnWallet.getId()).get();
    }
  }

   */
