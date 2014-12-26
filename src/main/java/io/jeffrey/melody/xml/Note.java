package io.jeffrey.melody.xml;

import java.util.Map;

import io.jeffrey.melody.xml.helpers.Mapify;

import org.w3c.dom.Node;

public class Note {

	public Pitch pitch;
	public boolean rest;
	public boolean tieStart;
	public boolean tieEnd;

	public int duration;
	public int staff;

	public Note() {
		this.pitch = null;
		this.rest = false;
		this.tieStart = false;
		this.tieEnd = false;
		this.duration = 1;
		this.staff = 1;
	}

	public Note(Note last) {
		this.duration = last.duration;
		this.staff = last.staff;
	}

	public Note(Node node, Note last) {
		this(last);
		Map<String, Node> easy = Mapify.asMap(node);

		if (easy.containsKey("pitch")) {
			this.pitch = new Pitch(easy.get("pitch"));
		} else if (easy.containsKey("rest")) {
			this.rest = true;
		}

		for (Node tie : Mapify.ofName(node, "tie")) {
			if (tie.hasAttributes()) {
				if ("start".equalsIgnoreCase(tie.getAttributes()
						.getNamedItem("type").getTextContent())) {
					tieStart = true;
				}
				if ("end".equalsIgnoreCase(tie.getAttributes()
						.getNamedItem("type").getTextContent())) {
					tieEnd = true;
				}
			}
		}

		if (easy.containsKey("duration")) {
			duration = Integer.parseInt(easy.get("duration").getTextContent());
		}
		if (easy.containsKey("staff")) {
			staff = Integer.parseInt(easy.get("staff").getTextContent());
		}
	}
	
	@Override
	public String toString() {
		if(rest) return "[rest]";
		if(pitch != null) return pitch.toString();
		return "?";
	}
	
}
