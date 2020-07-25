package com.kudos.server.components;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.kudos.server.model.jpa.KudosCard;
import com.kudos.server.model.jpa.KudosType;
import com.kudos.server.model.dto.imports.ConfluenceCard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConfluenceImporter {
  private final Logger logger = LoggerFactory.getLogger(ConfluenceImporter.class);

  public List<KudosCard> importCards(URL source) {
    List<KudosCard> result = new ArrayList<>();
    ObjectMapper input = new ObjectMapper();
    try {
      JsonNode formdata = input.readTree(source);
      ArrayNode rowData = (ArrayNode) formdata.get("rows");
      for (JsonNode rowDatum : rowData) {
        ConfluenceCard card = input.readValue(rowDatum.toString(), ConfluenceCard.class);
        result.add(convert(card));
        logger.info("read card from: "+card.userName);
      }


    } catch (IOException e) {
      logger.error("could not import kudos card", e);
    }

    return result;
  }

  public static KudosCard convert(ConfluenceCard card) {
    KudosCard result = new KudosCard();
    result.setImporterID(card.id);
    result.setCreated(DateTimeFormatter.ISO_DATE_TIME.parse(card.created, Instant::from));
    result.setEdited(DateTimeFormatter.ISO_DATE_TIME.parse(card.updated, Instant::from));

    result.setType(mapType(card.fields.get(0).values[0]));
    result.setMessage(card.fields.get(1).values[0]);
    result.setWriter(card.fields.get(2).values[0]);
    return result;
  }

  public static KudosType mapType(String value) {
    switch (value) {
      case "Awesome" : return KudosType.AWESOME;
      case "Appreciation" : return KudosType.APPRECIATION;
      case "Happy" : return KudosType.HAPPY;
      default:
      return KudosType.THANK_YOU;
    }
  }
}
