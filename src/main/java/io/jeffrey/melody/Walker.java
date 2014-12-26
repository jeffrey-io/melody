package io.jeffrey.melody;

import io.jeffrey.melody.xml.Note;
import io.jeffrey.melody.xml.helpers.Mapify;

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
	private MeasureWalk measureWalker;

	public Walker() {
		this.measureWalker = new MeasureWalk();
	}

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

	private Note priorNote = new Note();

	private void measurePart(Node node) throws Exception {
		if (node.getNodeType() == Node.TEXT_NODE)
			return;
		String name = node.getNodeName().toLowerCase();

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
		if("backup".equals(name)) {
			int backup = Integer.parseInt(			Mapify.asStringMap(node).get("duration"));
			measureWalker.backup(backup);
		}
		if ("note".equals(name)) {
			Note current = new Note(node, priorNote);
			measureWalker.note(current);
			priorNote = current;
			return;
		}
		System.out.println("+ " + name);
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
				throw new Exception("Melody only supports one part for now");
			return;
		} else if ("part".equals(name)) {
		} else if ("".equals(name)) {
		} else if ("".equals(name)) {
		} else if ("measure".equals(name)) {
			NodeList nodeList = node.getChildNodes();
			measureWalker.begin();
			for (int k = 0; k < nodeList.getLength(); k++)
				measurePart(nodeList.item(k));
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

}
