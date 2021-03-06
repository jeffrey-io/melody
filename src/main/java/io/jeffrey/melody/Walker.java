package io.jeffrey.melody;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.jeffrey.melody.data.NoteStruct;
import io.jeffrey.melody.xml.helpers.Mapify;

public class Walker {
  private static final Set<String> ignore      = new HashSet<String>(Arrays.asList("credit", "identification", "print"));
  HashSet<String>                  names       = new HashSet<String>();
  private int                      numberParts = 0;
  private final MeasureWalk        measureWalker;

  private NoteStruct               priorNote   = new NoteStruct();

  public Walker() throws Exception {
    this.measureWalker = new MeasureWalk();
  }

  private void measurePart(final Node node) throws Exception {
    if (node.getNodeType() == Node.TEXT_NODE) {
      return;
    }
    final String name = node.getNodeName().toLowerCase();

    if ("print".equals(name)) {
      return;
    }
    if ("barline".equals(name)) {
      return;
    }
    if ("attributes".equals(name)) {
      // pull out data about the measure
      return;
    }
    if ("direction".equals(name)) {
      // pull out tempo
      return;
    }
    if ("backup".equals(name)) {
      final int backup = Integer.parseInt(Mapify.asStringMap(node).get("duration"));
      measureWalker.backup(backup);
      return;
    }
    if ("note".equals(name)) {
      final NoteStruct current = new NoteStruct(node, priorNote);
      measureWalker.note(current);
      priorNote = current;
      return;
    }
    System.out.println("+ " + name);
  }

  public void walk(final Document document) throws Exception {
    numberParts = 0;
    if (document.hasChildNodes()) {
      walk(document.getChildNodes());
    }
  }

  public void walk(final Node node) throws Exception {
    final String name = node.getNodeName().toLowerCase();

    if (ignore.contains(name)) {
      return;
    }
    if (node.getNodeType() == Node.TEXT_NODE) {
      return;
    }

    final boolean element = node.getNodeType() == Node.ELEMENT_NODE;
    if ("score-partwise".equals(name)) {
      if (element) {

      }
    } else if ("part-list".equals(name)) {
    } else if ("defaults".equals(name)) {
      return;
    } else if ("score-part".equals(name)) {
      numberParts++;
      if (numberParts > 1) {
        throw new Exception("Melody only supports one part for now");
      }
      return;
    } else if ("part".equals(name)) {
    } else if ("".equals(name)) {
    } else if ("".equals(name)) {
    } else if ("measure".equals(name)) {
      final NodeList nodeList = node.getChildNodes();
      measureWalker.begin();
      for (int k = 0; k < nodeList.getLength(); k++) {
        measurePart(nodeList.item(k));
      }
      measureWalker.end();
      return;
    } else {
      System.out.println("unhandled:" + name);
      return;
    }
    if (node.hasChildNodes()) {
      walk(node.getChildNodes());
    }
    names.add(name);
  }

  public void walk(final NodeList nodeList) throws Exception {
    for (int k = 0; k < nodeList.getLength(); k++) {
      walk(nodeList.item(k));
    }
  }

}
