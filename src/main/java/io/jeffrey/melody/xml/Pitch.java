package io.jeffrey.melody.xml;

import java.util.Map;

import io.jeffrey.melody.xml.helpers.Mapify;

import org.w3c.dom.Node;

public class Pitch {

	public static enum Step {
		C(0), D(1), E(2), F(3), G(4), A(5), B(6);

		public final int asInteger;

		private Step(int i) {
			this.asInteger = i;
		}
	}

	public final Step step;
	public final int octave;

	public Pitch(Node node) {
		Map<String, String> data = Mapify.asStringMap(node);
		step = Step.valueOf(data.get("step").toUpperCase());
		octave = Integer.parseInt(data.get("octave"));
		// TODO: alter
	}

	@Override
	public String toString() {
		return "{" + step + "@" + octave + "}";
		// TODO add alter
	}
}
