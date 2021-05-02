package com.kudos.server;

import com.kudos.server.controller.Util;
import org.junit.jupiter.api.Test;

public class Md5Test {
  @Test
  void testMd5() {
    assert Util.MD5("xxx").equals("{MD5}f561aaf6ef0bf14d4208bb46a4ccb3ad");
  }
}
