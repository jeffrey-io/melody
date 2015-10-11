package io.jeffrey.melody.data;

import java.util.Map;

import org.w3c.dom.Node;

import io.jeffrey.melody.xml.helpers.Mapify;

public class Pitch {

  public static enum Step {
    C(0), D(1), E(2), F(3), G(4), A(5), B(6);

    public final int asInteger;

    private Step(final int i) {
      this.asInteger = i;
    }
  }
  
  public static enum Alter {
    Flat, Normal, Sharp
  }

  public final Step step;
  public final int  octave;
  public final Alter alter;

  public Pitch(final Node node) {
    final Map<String, String> data = Mapify.asStringMap(node);
    step = Step.valueOf(data.get("step").toUpperCase());
    octave = Integer.parseInt(data.get("octave"));
    
    String alter = "0";
    
    if (data.containsKey("alter")) {
      alter = data.get("alter");
    }
    
    if (alter.equals("1")) {
      this.alter = Alter.Sharp;
    } else     if (alter.equals("-1")) {
      this.alter = Alter.Flat;
    } else {
      this.alter = Alter.Normal;
    }
  }
  
  @Override
  public String toString() {
    return "{" + step + "@" + octave + "}/" + alter;
    // TODO add alter
  }
}
