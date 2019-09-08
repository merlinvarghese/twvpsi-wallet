package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ComponentScan(basePackageClasses = {
    WalletService.class
})
class WalletServiceMockTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private WalletService walletService;

  @Test
  void shouldCreateWalletWithMockWalletService() throws Exception {
    when(walletService.createWallet(any(Wallet.class))).thenReturn(new Wallet("George", 1000));

    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"George\",\"balance\":1000}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"name\":\"George\",\"balance\":1000}"));

    verify(walletService).createWallet(any(Wallet.class));

  }
  @Test
  void shouldListAllWalletsWithMockWalletService() throws Exception {
    when(walletService.listWallets()).thenReturn(new Wallet[]{
        new Wallet("George", 2000),
        new Wallet("Merlin", 1000)
    });

    mockMvc.perform(get("/wallets/list")).andExpect(status().isOk())
        .andExpect(content().json("[{\"name\":\"George\",\"balance\":2000.0}," +
            "{\"name\":\"Merlin\",\"balance\":1000.0}]"));

    verify(walletService).listWallets();
  }


  @Test
  void shouldDeleteWalletWithMockWalletService() throws Exception {
    when(walletService.removeWallet("Merlin")).thenReturn(new String("Merlin"));

    mockMvc.perform(post("/wallets/delete")
        .param("name","Merlin")
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andDo(print()).andReturn().getResponse().getContentAsString();

    verify(walletService).removeWallet("Merlin");
  }


  @Test
  void shouldGetWalletDetailsForAUserWithMockWalletService() throws Exception {
    when(walletService.getWalletDetails("Merlin")).thenReturn(new Wallet("Merlin", 1000));

    mockMvc.perform(post("/wallets/getWalletDetails")
        .param("name","Merlin")
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(content().json("{\"name\":\"Merlin\",\"balance\":1000}"));

    verify(walletService).getWalletDetails("Merlin");
  }
}
