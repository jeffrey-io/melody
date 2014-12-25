package io.jeffrey.melody;

import java.util.HashSet;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Walker {
	HashSet<String> names = new HashSet<String>();

	public void walk(Document document) {
		if (document.hasChildNodes()) {
			walk(document.getChildNodes());
		}
		System.out.println("BEGIN:");
		for(String name : names) {
			System.out.println("Name:" + name);
		}
	}

	public void walk(NodeList nodeList) {
		for (int k = 0; k < nodeList.getLength(); k++)
			walk(nodeList.item(k));
	}

	public void walk(Node node) {
		String name = node.getNodeName().toLowerCase();
		names.add(name);
		if (node.hasChildNodes()) {
			walk(node.getChildNodes());
		}
	}

}
