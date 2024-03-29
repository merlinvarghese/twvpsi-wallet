package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ComponentScan(basePackageClasses = {
    WalletService.class
})
@WebMvcTest
@Validated
class WalletControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private WalletService walletService;

  @Test
  void expectWalletCreatedForAUser() throws Exception {
    Wallet wallet = new Wallet(1, "Merlin", 1000);
    when(walletService.createWallet(any(Wallet.class))).thenReturn(wallet);

    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"Merlin\",\"balance\":1000}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"id\":1,\"name\":\"Merlin\",\"balance\":1000}"));

    verify(walletService).createWallet(any(Wallet.class));
  }

  @Test
  void expectAllWalletsReturnedWhenNoIDGiven() throws Exception {
    List<Wallet> wallets = Arrays.asList(
        new Wallet(1, "George", 1000.0),
        new Wallet(2, "Joseph", 2000.0));
    when(walletService.getAllWallets()).thenReturn(wallets);

    mockMvc.perform(get("/wallets")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("[{\"id\":1,\"name\":\"George\",\"balance\":1000.0}, " +
            "{\"id\":2,\"name\":\"Joseph\",\"balance\":2000.0}]"));

    verify(walletService).getAllWallets();
  }

  @Test
  void expectWalletReturnedWithGivenUserId() throws Exception {

    when(walletService.getWalletById((long) 1)).thenReturn(new Wallet("Merlin", 1000));

    mockMvc.perform(get("/wallets/{id}", "1")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"name\":\"Merlin\",\"balance\":1000}"));

    verify(walletService).getWalletById(any(Long.class));
  }

  @Test
  void expectWalletDeletedWithGivenUserId() throws Exception {
    mockMvc.perform(delete("/wallets/{id}", "1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(walletService).deleteWallet(any(Long.class));
  }

  @Test
  void expectTransactionsCreatedOnWallet() throws Exception {
    Transactions transactions = new Transactions(Transactions.TYPE.CREDIT, 1000);
    when(walletService.performTransaction(any(Transactions.class), any(Long.class))).thenReturn(transactions);

    mockMvc.perform(post("/wallets/1/transactions")
        .content("{\"transactionType\":\"CREDIT\",\"amount\":100}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated());

    verify(walletService).performTransaction(any(Transactions.class), any(Long.class));
  }

  @Test
  void expectWalletNotFoundExceptionWhenRequestedWalletNotExist() throws Exception {
    when(walletService.getWalletById(1L)).thenThrow(new NoWalletsFoundException("Wallet Not Found"));

    mockMvc.perform(get("/wallets/{id}", "1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    verify(walletService).getWalletById(1L);
  }

  @Test
  void expectWalletNotFoundExceptionWhenTryingToDeleteNonExistingWallet() throws Exception {
    doThrow(NoWalletsFoundException.class).when(walletService).deleteWallet(1L);

    mockMvc.perform(delete("/wallets/{id}", "1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  void expectNoWalletFoundWhenTryingToGetTransactionsForWalletWithNoTransactions() throws Exception {
    Wallet wallet = new Wallet(1L, "Merlin", 1000.0);
    walletService.createWallet(wallet);
    when(walletService.getAllTransactions(1L)).thenThrow(new NoWalletsFoundException("No wallet found"));
    mockMvc.perform(get("/wallets/1/transactions")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    verify(walletService).getAllTransactions(1L);
  }

  @Test
  void expectInsufficientBalanceExceptionWhenRequestedAmountNotPresent() throws Exception {
    Wallet wallet = new Wallet(1L, "Merlin", 100.0);
    walletService.createWallet(wallet);

    when(walletService.performTransaction(any(Transactions.class), anyLong())).thenThrow(new InsufficientBalanceException());

    mockMvc.perform(post("/wallets/1/transactions")
        .content("{\"transactionType\":\"DEBIT\",\"amount\":1500}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity());
    verify(walletService).performTransaction(any(Transactions.class), anyLong());
  }

  @Test
  void expectBadRequestWhenUserNameIsEmpty() throws Exception {
    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"\", \"Balance\":\"200\"}")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors", hasItem("Please provide a name")));

    verify(walletService, never()).createWallet(any(Wallet.class));
  }

  @Test
  void expectBadRequestWhenBalanceIsNegative() throws Exception {
    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"Merlin\", \"balance\":\"-100\"}")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    verify(walletService, never()).createWallet(any(Wallet.class));
  }

  @Test
  void expectBadRequestWhenUserNameIsTooLong() throws Exception {
    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"NameIsTooooLong\", \"balance\":\"100\"}")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    verify(walletService, never()).createWallet(any(Wallet.class));
  }
}

