package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class WalletServiceTest {

  @Autowired
  private WalletRepository walletRepository;

  @Autowired
  private WalletService walletService;

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
  }
}
