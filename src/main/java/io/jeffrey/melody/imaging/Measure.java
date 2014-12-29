package io.jeffrey.melody.imaging;

import io.jeffrey.melody.data.NoteStruct;

import java.awt.Graphics;
import java.util.ArrayList;

public class Measure {
	private class PlottedNote {
		private final NoteStruct note;
		private final int x;
		private final int y;

		public PlottedNote(NoteStruct note, int x, int y) {
			this.note = note;
			this.x = x;
			this.y = y;
		}
	}

	private final ArrayList<PlottedNote> notes;

	public Measure() {
		this.notes = new ArrayList<Measure.PlottedNote>();
	}

	public void addNote(NoteStruct note, int x, int y) {
		notes.add(new PlottedNote(note, x, y));
	}

	public void draw(Graphics g, UnicodeNotes un) {
		for (PlottedNote pn : notes) {
			if (pn.note.rest) {
				un.plot(UnicodeNotes.Note.Rest_1_4, pn.x, pn.y, g);
			} else {
				un.plot(UnicodeNotes.Note.Note_1_4, pn.x, pn.y, g);
			}
		}
	}
}
