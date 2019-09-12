package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ComponentScan(basePackageClasses = {
    WalletService.class
})
@WebMvcTest
class WalletControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  WalletService walletService;

  @Test
  void shouldCreateWalletForAUser() throws Exception {
    Wallet wallet = new Wallet(1, "Merlin", 1000);
    ResponseEntity<Wallet> response = new ResponseEntity<>(wallet, HttpStatus.CREATED);
    when(walletService.createWallet(any(Wallet.class))).thenReturn(wallet);

    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"Merlin\",\"balance\":1000}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"id\":1,\"name\":\"Merlin\",\"balance\":1000}"));

    verify(walletService).createWallet(any(Wallet.class));
  }

  @Test
  void shouldReturnAllWalletsWhenNoIdGiven() throws Exception {
    List<Wallet> wallets = Arrays.asList(
        new Wallet(1, "George", 1000.0),
        new Wallet(2, "Joseph", 2000.0));
    when(walletService.getAllWallets()).thenReturn(wallets);

    mockMvc.perform(get("/wallets")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("[{\"name\":\"George\",\"balance\":1000.0}, {\"name\":\"Joseph\",\"balance\":2000.0}]"));

    verify(walletService).getAllWallets();
  }

}


/*
  @Test
  void shouldReturnAWalletWithGivenUserId() throws Exception {

    when(walletService.getWalletById((long) 1)).thenReturn(new Wallet("Merlin", 1000));

    mockMvc.perform(get("/wallets/{id}", "1")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"name\":\"Merlin\",\"balance\":1000}"));

    verify(walletService).getWalletById(any(Long.class));
  }

  @Test
  void shouldReturnAWalletWithGivenUserName() throws Exception {
    when(walletService.getWalletByName("Merlin")).thenReturn(new Wallet( "Merlin", 1000));

    mockMvc.perform(get("/wallets?name=Merlin")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("[{\"name\":\"Merlin\",\"balance\":1000.0}]"));

    verify(walletService).getWalletByName(any(String.class));
   }

  @Test
  void shouldReturnAllWalletsWhenNoUseridGiven() throws Exception {
    List<Wallet> wallets = Arrays.asList(
        new Wallet(1, "George", 1000.0),
        new Wallet(2, "Joseph", 1000.0));
    when(walletService.getAllWallets()).thenReturn(wallets);

    mockMvc.perform(get("/wallets")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("[{\"name\":\"George\",\"balance\":1000.0}, {\"name\":\"Joseph\",\"balance\":1000.0}]"));

    verify(walletService).getAllWallets();
  }

  @Test
  void shouldDeleteWalletWithGivenUserid() throws Exception {
    mockMvc.perform(delete("/wallets/{id}", "1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(walletService).deleteWallet(any(Long.class));
  }

  @Test
  void shouldCreateTransactionOnWallet() throws Exception {
    Transaction transaction = new Transaction(Transaction.TransactionType.CREDIT, 1000);
    when(walletService.createTransaction(any(Transaction.class), any(Long.class))).thenReturn(transaction);

    mockMvc.perform(post("/wallets/1/transactions")
        .content("{\"transactionType\":\"CREDIT\",\"amount\":100}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    verify(walletService).createTransaction(any(Transaction.class), any(Long.class));
  }

  @Test
  void shouldHandleExceptionWhenWalletWithIdNotFound() throws Exception {
    when(walletService.getWalletById(10L)).thenThrow(NoWalletsFoundException.class);

    mockMvc.perform(get("/wallets/10")).andExpect(status().isNotFound());
    verify(walletService).getWalletById(10L);
  }
  /*

  /*
  @Test
  void shouldHandleExceptionWhenTryingToDeleteAWalletNotPresent() throws Exception {
    //when(walletService.deleteWallet(10L)).thenThrow(NoWalletsFoundException.class);
    doThrow(NoWalletsFoundException).when();
    mockMvc.perform(get("/wallets/10")).andExpect(status().isNotFound());


  }

  @Test
  void shouldHandleExceptionWhenNoTransactionsFoundForAWallet() throws Exception {
    when(walletService.getTransactions(10L)).thenThrow(NoWalletsFoundException.class);

    mockMvc.perform(get("/wallets/10")).andExpect(status().isNotFound());
    verify(walletService).getWalletById(10L);
  }

   */



