package com.kudos.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Component
@ConfigurationProperties(prefix="kudos")
public class AppConfig {

  private Path basedir;
  private String cornerTitle;
  private String greeting;
  private String passwordHash;
  private String localeTag = "en";
  private String outro;


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

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getGreeting() {
    return greeting;
  }

  public void setGreeting(String greeting) {
    this.greeting = greeting;
  }

  public Locale getLocale() {
    return Locale.forLanguageTag(localeTag);
  }

  public String getLocaleTag() {
    return localeTag;
  }

  public void setLocaleTag(String localeTag) {
    this.localeTag = localeTag;
  }

  public String getOutro() {
    return outro;
  }

  public void setOutro(String outro) {
    this.outro = outro;
  }
}
