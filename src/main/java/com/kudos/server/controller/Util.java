package com.kudos.server.controller;

import com.kudos.server.config.AppConfig;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class Util {
  public static ZonedDateTime localDateTime(Instant time) {
    return time.atZone(ZoneId.systemDefault());
  }

  public static String formatDateTimeMedium(Instant edited, AppConfig appConfig) {
    return localDateTime(edited).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(appConfig.getLocale()));
  }

  public static String formatDate(Instant edited, AppConfig appConfig) {
    return localDateTime(edited).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(appConfig.getLocale()));
  }

  public static String MD5(String md5) {
    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
      byte[] array = md.digest(md5.getBytes());
      StringBuilder sb = new StringBuilder();
      sb.append("{MD5}");
      for (byte b : array) {
        sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
      }
      return sb.toString();
    } catch (java.security.NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
  }
}
