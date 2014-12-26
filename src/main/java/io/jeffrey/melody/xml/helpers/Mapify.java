package io.jeffrey.melody.xml.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Mapify {

	public static List<Node> ofName(Node node, String name) {
		ArrayList<Node> nodes = new ArrayList<Node>();
		NodeList nodeList = node.getChildNodes();
		for (int k = 0; k < nodeList.getLength(); k++) {
			Node child = nodeList.item(k);
			if (name.equals(child.getNodeName())) {
				nodes.add(child);
			}
		}
		return Collections.unmodifiableList(nodes);
	}

	public static Map<String, Node> asMap(Node node) {
		HashMap<String, Node> result = new HashMap<String, Node>();
		NodeList nodeList = node.getChildNodes();
		for (int k = 0; k < nodeList.getLength(); k++) {
			Node child = nodeList.item(k);
			result.put(child.getNodeName(), child);
		}
		return Collections.unmodifiableMap(result);
	}

	public static Map<String, String> asStringMap(Node node) {
		HashMap<String, String> result = new HashMap<String, String>();
		NodeList nodeList = node.getChildNodes();
		for (int k = 0; k < nodeList.getLength(); k++) {
			Node child = nodeList.item(k);
			result.put(child.getNodeName(), child.getTextContent());
		}
		return Collections.unmodifiableMap(result);
	}
}
