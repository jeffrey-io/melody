package io.jeffrey.melody;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Walker {
	HashSet<String> names = new HashSet<String>();
	private static final Set<String> ignore = new HashSet<String>(
			Arrays.asList("credit", "identification", "print"));
	private int numberParts = 0;

	public void walk(Document document) throws Exception {
		numberParts = 0;
		if (document.hasChildNodes()) {
			walk(document.getChildNodes());
		}
	}

	public void walk(NodeList nodeList) throws Exception {
		for (int k = 0; k < nodeList.getLength(); k++)
			walk(nodeList.item(k));
	}

	private void startMeasure() throws Exception {
		System.out.println("Starting measure:");
	}

	private void measurePart(Node node) throws Exception {
		if (node.getNodeType() == Node.TEXT_NODE)
			return;
		String name = node.getNodeName().toLowerCase();
		System.out.println("+ " + name);
		
	}

	private void endMeasure() throws Exception {
		System.out.println("Ending Measure");
		throw new Exception("err");

	}

	public void walk(Node node) throws Exception {
		String name = node.getNodeName().toLowerCase();

		if (ignore.contains(name))
			return;
		if (node.getNodeType() == Node.TEXT_NODE)
			return;

		boolean element = node.getNodeType() == Node.ELEMENT_NODE;
		if ("score-partwise".equals(name)) {
			if (element) {

			}
		} else if ("part-list".equals(name)) {
		} else if ("defaults".equals(name)) {
			return;
		} else if ("score-part".equals(name)) {
			numberParts++;
			if (numberParts > 1)
				throw new Exception();
			return;
		} else if ("part".equals(name)) {
		} else if ("".equals(name)) {
		} else if ("".equals(name)) {
		} else if ("measure".equals(name)) {
			NodeList nodeList = node.getChildNodes();
			startMeasure();
			for (int k = 0; k < nodeList.getLength(); k++)
				measurePart(nodeList.item(k));
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

}
