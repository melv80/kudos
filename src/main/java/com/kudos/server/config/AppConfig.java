package com.kudos.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConfigurationProperties(prefix="kudos")
public class AppConfig {

  private Path basedir;
  private String cornerTitle;
  private String greeting;

  public Path getBasedir() {
    return basedir;
  }

  public void setBasedir(String basedir) {
    this.basedir = Paths.get(basedir).toAbsolutePath();
  }

  public String getCornerTitle() {
    return cornerTitle;
  }

  public void setCornerTitle(String cornerTitle) {
    this.cornerTitle = cornerTitle;
  }

  public String getGreeting() {
    return greeting;
  }

  public void setGreeting(String greeting) {
    this.greeting = greeting;
  }
}
