package com.kudos.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class KudosApplication {

  public static void main(String[] args) {
    SpringApplication.run(KudosApplication.class, args);
  }

}
