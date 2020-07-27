package com.kudos.server.components;

import com.kudos.server.model.jpa.KudosCard;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.List;

public class ConfluenceImporterTest {

  ConfluenceImporter importer = new ConfluenceImporter();

  @Test

  void testOneImport() throws MalformedURLException {
    List<KudosCard> cards = importer.importCardsFromJsonFile(new File("./demoimport/DemoImport.json").toURI().toURL());
    assert cards.size() == 2 : "wrong number of cards imported";
  }

  @Test
  void testAllImports() {
    List<KudosCard> cards = importer.importCardsFromDir(Paths.get("./demoimport"));
    assert cards.size() == 4 : "wrong number of cards imported";
  }
}
