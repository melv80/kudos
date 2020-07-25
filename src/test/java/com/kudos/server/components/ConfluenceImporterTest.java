package com.kudos.server.components;

import com.kudos.server.model.jpa.KudosCard;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public class ConfluenceImporterTest {

  ConfluenceImporter importer = new ConfluenceImporter();

  @Test

  void test() throws MalformedURLException {
    List<KudosCard> cards = importer.importCards(new File("./demoimport/DemoImport.json").toURI().toURL());
    assert cards.size() == 2 : "wrong number of cards imported";
  }
}
