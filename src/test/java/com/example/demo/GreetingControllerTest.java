package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ComponentScan(basePackageClasses = {
    WalletService.class
})
class GreetingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  //@Test
  void expectGetOnGreeting() throws Exception {
    mockMvc.perform(get("/greeting")).andExpect(status().isOk())
        .andExpect(content().string("Hello World"));

  }

  //@Test
  void expectWelcomeUser() throws Exception {
    mockMvc.perform(get("/greeting/welcome?userName=Merlin")).andExpect(status().isOk())
        .andExpect(content().string("Hello Merlin"));

  }

}