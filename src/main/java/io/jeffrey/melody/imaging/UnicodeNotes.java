package io.jeffrey.melody.imaging;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class UnicodeNotes {
  public static enum Note {
    TrebelClef(utf(0x1D11E)), //
    BaseClef(utf(0x1D122)), //
    Rest_1_1(utf(0x1D13B)), //
    Rest_1_2(utf(0x1D13C)), //
    Rest_1_4(utf(0x1D13D)), //
    Rest_1_8(utf(0x1D13E)), //
    Rest_1_16(utf(0x1D13F)), //
    Rest_1_32(utf(0x1D140)), //
    Rest_1_64(utf(0x1D141)), //

    Stemless_1_2(utf(0x1D157)), //
    Stemless_1_4(utf(0x1D158)), //

    Note_1_1(utf(0x1D15D)), //
    Note_1_2(utf(0x1D15E)), //
    Note_1_4(utf(0x1D15F)), //
    Note_1_8(utf(0x1D160)), //
    Note_1_16(utf(0x1D161)), //
    Note_1_32(utf(0x1D162)), //
    Note_1_64(utf(0x1D163)), //
    Note_1_128(utf(0x1D164)), //

    Flat("\u266D"), //
    Natural("\u266E"), //
    Sharp("\u266F"), //

    CrossedOutTinyNote(utf(0x1D194)), //
    TinyNote(utf(0x1D195)); //

    public final String str;

    private Note(final String _str) {
      this.str = _str;
    }
  }

  private static String utf(final int codePoint) {
    return new String(new char[] { Character.highSurrogate(codePoint), Character.lowSurrogate(codePoint) });
  }

  private final Font                   font;
  private final BufferedImage          scratch;
  private final Graphics               graphics;
  private final Map<Note, Rectangle2D> sizes;

  public final int                     noteWidth;
  public final int                     halfNoteHeight;

  public final int                     upstemOffsetX;

  public final int                     upstemOffsetY;
  public final int                     stemHeight;

  public UnicodeNotes(final Font font) throws Exception {
    this.font = font;
    this.scratch = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
    this.graphics = scratch.getGraphics();
    this.sizes = new HashMap<Note, Rectangle2D>();
    for (final Note note : Note.values()) {
      final Rectangle2D bounds = graphics.getFontMetrics(font).getStringBounds(note.str, graphics);
      sizes.put(note, bounds);
      if (Math.abs(bounds.getX()) > 0.01) {
        throw new RuntimeException("A positive x value was found");
      }
    }
    graphics.setFont(font);
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, 128, 128);
    final Rectangle2D whole = sizes.get(Note.Note_1_1);
    graphics.setColor(Color.BLACK);
    graphics.drawString(Note.Note_1_1.str, 0, 1 + (int) whole.getHeight());
    int minY = 100000;
    int maxY = -1;
    for (int x = 0; x <= whole.getWidth() * 2; x++) {
      for (int y = 0; y <= whole.getHeight() + Math.abs(whole.getY()) + 1; y++) {
        final Color at = new Color(scratch.getRGB(x, y));
        if (at.getBlue() < 64) {
          minY = Math.min(minY, y);
          maxY = Math.max(maxY, y);
        }
      }
    }
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, 128, 128);
    graphics.setColor(Color.BLACK);
    final int atY = 1 + (int) whole.getHeight();
    graphics.drawString(Note.Note_1_4.str, 0, atY);
    graphics.setColor(Color.WHITE);
    graphics.drawString(Note.Stemless_1_4.str, 0, atY);

    this.noteWidth = (int) Math.ceil(whole.getWidth());
    this.halfNoteHeight = (int) Math.ceil((maxY - minY) / 2.0);

    minY = 100000;
    maxY = -1;
    int minX = 100000;
    int maxX = -1;

    for (int x = 0; x <= whole.getWidth() * 2; x++) {
      for (int y = 0; y <= whole.getHeight() + Math.abs(whole.getY()) + 1; y++) {
        final Color at = new Color(scratch.getRGB(x, y));
        if (at.getBlue() < 1) {
          minX = Math.min(minX, x);
          maxX = Math.max(maxX, x);
        }
      }
    }
    for (int x = maxX - 1; x <= whole.getWidth() * 2; x++) {
      for (int y = 0; y <= whole.getHeight() + Math.abs(whole.getY()) + 1; y++) {
        final Color at = new Color(scratch.getRGB(x, y));
        if (at.getBlue() < 8) {
          minY = Math.min(minY, y);
          maxY = Math.max(maxY, y);
        }
      }
    }

    upstemOffsetX = (int) ((minX + maxX * 9) / 10.0);
    upstemOffsetY = minY - atY;
    stemHeight = maxY - minY;
  }

  public void plot(final Note note, final int x, final int y, final Graphics graphics) {
    graphics.setFont(font);
    graphics.drawString(note.str, x, y);
  }

  public void plot(final Note a, final Note b, final int x, final int y, final Graphics graphics) {
    graphics.setFont(font);
    graphics.drawString(a.str, (int) Math.ceil(x - sizes.get(a).getWidth() - 1), y);
    graphics.drawString(b.str, x, y);
  }

  public void stem(final boolean down, final int x, final int y, final Graphics graphics) {
    if (down) {
      for (int dx = 0; dx < 2; dx++) {
        graphics.drawLine(x + dx, y, x + dx, y + stemHeight);
      }
    } else {
      for (int dx = 0; dx < 2; dx++) {
        graphics.drawLine(dx + x + upstemOffsetX, y + upstemOffsetY, dx + x + upstemOffsetX, y + upstemOffsetY + stemHeight);
      }
    }
  }

  public double width(final Note note) {
    return sizes.get(note).getWidth();
  }
}
