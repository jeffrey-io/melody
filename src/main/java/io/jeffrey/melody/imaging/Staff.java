package io.jeffrey.melody.imaging;

import io.jeffrey.melody.imaging.UnicodeNotes.Note;

import java.awt.Graphics;

public class Staff {

	private final UnicodeNotes notes;

	public Staff(UnicodeNotes notes) {
		this.notes = notes;
	}

	public void drawStaff(Graphics g, Note clef, int clefLine, int topY, int x1, int x2) {
		int y = topY;
		for (int k = 0; k < 5; k++) {
			g.drawLine(x1, y, x2, y);
			if(clefLine == k + 1) {
				notes.plot(clef, x1, y, g);
			}
			y += notes.halfNoteHeight * 2;
		}
	}
}
