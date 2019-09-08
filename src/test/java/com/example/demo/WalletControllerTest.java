package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ComponentScan(basePackageClasses = {
    WalletService.class
})
@WebMvcTest
class WalletControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldCreateWalletWithWalletService() throws Exception {

    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"Merlin\",\"balance\":1000}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"name\":\"Merlin\",\"balance\":1000}"));
  }

  @Test
  void shouldListAllWalletsWithWalletService() throws Exception {

    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"Merlin\",\"balance\":1000}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"name\":\"Merlin\",\"balance\":1000}"));

    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"George\",\"balance\":2000}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"name\":\"George\",\"balance\":2000}"));


    mockMvc.perform(get("/wallets/list")).andExpect(status().isOk())
        .andExpect(content().json("[{\"name\":\"Merlin\",\"balance\":1000.0}," +
            "{\"name\":\"George\",\"balance\":2000.0}]"));
  }


  @Test
  void shouldDeleteWalletWithWalletService() throws Exception {

    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"Merlin\",\"balance\":1000}")
        .contentType(MediaType.APPLICATION_JSON));

    mockMvc.perform(post("/wallets/delete")
        .param("name","Merlin")
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andDo(print()).andReturn().getResponse().getContentAsString();

    System.out.println("Delete done");
    mockMvc.perform(get("/wallets/list")).andExpect(status().isOk())
        .andDo(print()).andReturn().getResponse().getContentAsString();
  }


  @Test
  void shouldGetWalletDetailsWithWalletService() throws Exception {

    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"Merlin\",\"balance\":1000}")
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
        .andDo(print()).andReturn().getResponse().getContentAsString();

    mockMvc.perform(post("/wallets")
        .content("{\"name\":\"George\",\"balance\":2000}")
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
        .andDo(print()).andReturn().getResponse().getContentAsString();

    mockMvc.perform(post("/wallets/getWalletDetails")
        .param("name","Merlin")
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andDo(print()).andReturn().getResponse().getContentAsString();

  }
}
