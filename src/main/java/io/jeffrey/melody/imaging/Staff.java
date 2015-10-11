package io.jeffrey.melody.imaging;

import java.awt.Graphics;

import io.jeffrey.melody.imaging.UnicodeNotes.Note;

public class Staff {

  private final UnicodeNotes notes;

  public Staff(final UnicodeNotes notes) {
    this.notes = notes;
  }

  public void drawStaff(final Graphics g, final Note clef, final int clefLine, final int topY, final int x1, final int x2) {
    int y = topY;
    for (int k = 0; k < 5; k++) {
      g.drawLine(x1, y, x2, y);
      if (clefLine == k + 1) {
        notes.plot(clef, x1, y, g);
      }
      y += notes.halfNoteHeight * 2;
    }
  }
}
