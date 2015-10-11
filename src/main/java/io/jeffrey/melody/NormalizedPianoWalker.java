package io.jeffrey.melody;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import io.jeffrey.melody.data.NoteStruct;
import io.jeffrey.melody.xml.helpers.Mapify;

public abstract class NormalizedPianoWalker {
  private static final Set<String> ignore      = new HashSet<String>(Arrays.asList("credit", "identification", "print"));
  HashSet<String>                  names       = new HashSet<String>();
  private int                      numberParts = 0;
  private NoteStruct               priorNote   = new NoteStruct();

  public NormalizedPianoWalker(Document document) throws Exception {
    walk(document);
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
      final int duration = Integer.parseInt(Mapify.asStringMap(node).get("duration"));
      backup(duration);
      return;
    }
    if ("note".equals(name)) {
      final NoteStruct current = new NoteStruct(node, priorNote);
      priorNote = current;
      note(current);
      return;
    }
    unknown(name);
  }

  private void walk(final Document document) throws Exception {
    numberParts = 0;
    if (document.hasChildNodes()) {
      walk(document.getChildNodes());
    }
  }

  private void walk(final Node node) throws Exception {
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
    } else if ("measure".equals(name)) {
      final NodeList nodeList = node.getChildNodes();
      startMeasure();
      for (int k = 0; k < nodeList.getLength(); k++) {
        measurePart(nodeList.item(k));
      }
      endMeasure();
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

  private void walk(final NodeList nodeList) throws Exception {
    for (int k = 0; k < nodeList.getLength(); k++) {
      walk(nodeList.item(k));
    }
  }
  
  public abstract void startMeasure();
  
  public abstract void backup(int duration);
  
  public abstract void endMeasure();

  public abstract void note(NoteStruct note);
  
  public abstract void unknown(String name);
}
