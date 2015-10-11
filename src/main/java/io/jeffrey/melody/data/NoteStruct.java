package io.jeffrey.melody.data;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import io.jeffrey.melody.xml.helpers.Mapify;

public class NoteStruct {

  public Pitch                    pitch;
  public boolean                  rest;
  public boolean                  tieStart;
  public boolean                  tieEnd;
  public boolean                  chord;
  public int                      duration;
  public int                      staff;
  public HashMap<Integer, String> beams;

  public NoteStruct() {
    this.pitch = null;
    this.rest = false;
    this.tieStart = false;
    this.tieEnd = false;
    this.duration = 1;
    this.staff = 1;
    this.chord = false;
    this.beams = new HashMap<Integer, String>();
  }

  public NoteStruct(final Node node, final NoteStruct last) {
    this(last);
    final Map<String, Node> easy = Mapify.asMap(node);

    if (easy.containsKey("pitch")) {
      this.pitch = new Pitch(easy.get("pitch"));
    } else if (easy.containsKey("rest")) {
      this.rest = true;
    }

    for (final Node tie : Mapify.ofName(node, "tie")) {
      if (tie.hasAttributes()) {
        if ("start".equalsIgnoreCase(tie.getAttributes().getNamedItem("type").getTextContent())) {
          tieStart = true;
        }
        if ("end".equalsIgnoreCase(tie.getAttributes().getNamedItem("type").getTextContent())) {
          tieEnd = true;
        }
      }
    }

    if (easy.containsKey("chord")) {
      chord = true;
    }

    for (final Node beam : Mapify.ofName(node, "beam")) {
      int level = 1;
      if (beam.hasAttributes()) {
        final Node value = beam.getAttributes().getNamedItem("number");
        if (value != null) {
          level = Integer.parseInt(value.getTextContent());
        }
      }
      beams.put(level, beam.getTextContent());
    }
    if (easy.containsKey("duration")) {
      duration = Integer.parseInt(easy.get("duration").getTextContent());
    }
    if (easy.containsKey("staff")) {
      staff = Integer.parseInt(easy.get("staff").getTextContent());
    }
  }

  public NoteStruct(final NoteStruct last) {
    this();
    this.duration = last.duration;
    this.staff = last.staff;
  }

  @Override
  public String toString() {
    if (rest) {
      return "[rest]";
    }
    if (pitch != null) {
      return pitch.toString() + " " + this.duration;
    }
    return "?";
  }
}
