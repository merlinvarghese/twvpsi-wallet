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

  /*
  @Test
  void shouldCreateUserWithService() {
    Wallet wallet = new Wallet("Merlin", 500);

    Wallet expected = walletService.createWallet(wallet);

    Optional<Wallet> createdWallet = walletRepository.findById(expected.getId());
    Wallet actual = createdWallet.get();
    assertEquals(expected, actual);
  }


  @Test
  void shouldFindUserWithService() {
    Wallet wallet = new Wallet("Merlin", 500);
    walletService.createWallet(wallet);

    Optional<Wallet> createdWallet = walletRepository.findById((long)1);
    Wallet actual = createdWallet.get();

    assertEquals(wallet, actual);
  }*/
  @Autowired
  TransactionRepository transactionRepository;

  @BeforeEach
  void setup() {
    walletRepository.deleteAll();
    transactionRepository.deleteAll();
  }

  @Nested
  class WalletCreation {
    @Test
    void expectWalletCreatedWithGivenUserDetails() {
      Wallet firstWallet = new Wallet("George", 1000);

      Wallet createdWallet = walletService.createWallet(firstWallet);

      Wallet fetchedWallet = walletRepository.findById(createdWallet.getId()).get();

      assertNotNull(fetchedWallet);
      assertEquals(firstWallet.getName(), fetchedWallet.getName());
      assertEquals(firstWallet.getBalance(), fetchedWallet.getBalance());
    }
  }

  @Nested
  class FetchWallet {
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

      assertThrows(UserWalletDoesNotExistException.class, () -> walletService.getWalletByName("Sia"));
    }

    @Test
    void shouldReturnAllWallets() throws NoWalletsFoundException {
      Wallet firstWallet = new Wallet("George", 1000);
      Wallet secondWallet = new Wallet("Joseph", 1000);
      Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
      Wallet createdJosephWallet = walletService.createWallet(secondWallet);
      List<Wallet> expectedWallets = new LinkedList<>();
      expectedWallets.add(createdGeorgeWallet);
      expectedWallets.add(createdJosephWallet);

      List<Wallet> actualWallets = walletService.getAllWallets();

      assertEquals(expectedWallets, actualWallets);
    }

    @Test
    void shouldFailWhenNoWalletsExists() throws NoWalletsFoundException {
      assertThrows(NoWalletsFoundException.class, () -> walletService.getAllWallets());
    }
  }

  @Nested
  class DeleteWallet {
    @Test
    void expectWalletDeletedWithGivenUserId() throws UserWalletDoesNotExistException {
      Wallet firstWallet = new Wallet( "George", 1000);
      Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);

      assertDoesNotThrow(() -> walletService.deleteWallet(createdGeorgeWallet.getId()));
    }

    @Test
    void expectFailsWhenWalletWithGivenUserIdDoesNotExist() throws UserWalletDoesNotExistException {
      assertThrows(UserWalletDoesNotExistException.class, () -> walletService.deleteWallet((long) 100));
    }
  }

  @Nested
  class TransactionTests {
    @Test
    void shouldUpdateBalanceWithCreditTransaction() throws UserWalletDoesNotExistException {
      Wallet firstWallet = new Wallet("George", 1000);
      Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
      long createdWalletId = createdGeorgeWallet.getId();
      Transaction credit = new Transaction(Transaction.TransactionType.CREDIT, 100);

      Transaction createdTransaction = walletService.performTransaction(credit, createdWalletId);

      Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();
      assertTrue("1100.0".equals(fetchedWallet.getBalance()));
    }

    @Test
    void shouldCreateTransactionForWallet() throws UserWalletDoesNotExistException {
      Wallet firstWallet = new Wallet("George", 1000);
      Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
      long createdWalletId = createdGeorgeWallet.getId();
      Transaction credit = new Transaction(Transaction.TransactionType.CREDIT, 100);

      walletService.performTransaction(credit, createdWalletId);

      Wallet fetchedWallet = walletRepository.findById(createdWalletId).get();
      assertEquals(1, fetchedWallet.getTransactionsSize());
    }

    @Test
    void shouldCreatReferencesOfTransactionAndWalletOnEachOther() throws UserWalletDoesNotExistException {
      Wallet firstWallet = new Wallet("George", 1000);
      Wallet createdGeorgeWallet = walletService.createWallet(firstWallet);
      long createdWalletId = createdGeorgeWallet.getId();
      Transaction credit = new Transaction(Transaction.TransactionType.CREDIT, 100);

      walletService.performTransaction(credit, createdWalletId);

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
      walletService.performTransaction(credit, createdWalletId);

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
}