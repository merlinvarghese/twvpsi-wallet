package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
