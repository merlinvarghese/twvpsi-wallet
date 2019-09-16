package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greeting")
class GreetingController {

  @GetMapping
  String greeting() {
    return "Hello World";
  }

  @GetMapping("/welcome")
  String welcomeUser(@RequestParam(value = "userName") String name) {
    return "Hello " + name;
  }
}
