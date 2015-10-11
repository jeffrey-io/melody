package io.jeffrey.melody.xml.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Mapify {

  public static Map<String, Node> asMap(final Node node) {
    final HashMap<String, Node> result = new HashMap<String, Node>();
    final NodeList nodeList = node.getChildNodes();
    for (int k = 0; k < nodeList.getLength(); k++) {
      final Node child = nodeList.item(k);
      result.put(child.getNodeName(), child);
    }
    return Collections.unmodifiableMap(result);
  }

  public static Map<String, String> asStringMap(final Node node) {
    final HashMap<String, String> result = new HashMap<String, String>();
    final NodeList nodeList = node.getChildNodes();
    for (int k = 0; k < nodeList.getLength(); k++) {
      final Node child = nodeList.item(k);
      result.put(child.getNodeName(), child.getTextContent());
    }
    return Collections.unmodifiableMap(result);
  }

  public static List<Node> ofName(final Node node, final String name) {
    final ArrayList<Node> nodes = new ArrayList<Node>();
    final NodeList nodeList = node.getChildNodes();
    for (int k = 0; k < nodeList.getLength(); k++) {
      final Node child = nodeList.item(k);
      if (name.equals(child.getNodeName())) {
        nodes.add(child);
      }
    }
    return Collections.unmodifiableList(nodes);
  }
}
