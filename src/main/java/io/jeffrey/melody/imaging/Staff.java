package io.jeffrey.melody.imaging;

import java.awt.Graphics;

public class Staff {

	private final UnicodeNotes notes;

	public Staff(UnicodeNotes notes) {
		this.notes = notes;
	}

	public void drawStaff(Graphics g, int topY, int x1, int x2) {
		int y = topY;
		for (int k = 0; k < 5; k++) {
			g.drawLine(x1, y, x2, y);
			y += notes.halfNoteHeight * 2;
		}
	}
}
