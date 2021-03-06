package io.jeffrey.melody.imaging;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import io.jeffrey.melody.imaging.UnicodeNotes.Note;

public class Stamp {

  public static void main(final String[] args) throws Exception {
    final BufferedImage img = new BufferedImage(256, 256, BufferedImage.TYPE_INT_BGR);
    final Graphics g = img.getGraphics();

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, 256, 256);

    final Font f = new Font("Bravura", Font.PLAIN, 48);
    final UnicodeNotes notes = new UnicodeNotes(f);
    final Staff s = new Staff(notes);

    g.setColor(Color.BLACK);
    g.setFont(f);
    s.drawStaff(g, Note.TrebelClef, 4, 50, 0, 256);

    notes.plot(Note.Stemless_1_2, 50, 50, g);
    notes.stem(false, 50, 50, g);
    notes.stem(true, 50, 50, g);

    /*
     * notes.plot(Note.Note_1_1, 50, 50, g); notes.plot(Note.Flat, Note.Note_1_1, 50, 50, g); notes.plot(Note.Note_1_1, 75, 50 + notes.halfNoteHeight, g); notes.plot(Note.Sharp, Note.Note_1_1, 100, 50 + notes.halfNoteHeight * 2, g);
     * 
     * 
     * g.drawLine(0, 50, 256, 50); g.dispose(); ImageIO.write(img, "png", new File("C:\\Users\\jeff_000\\Desktop\\todo.png"));
     */
  }
}
