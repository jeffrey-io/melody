package io.jeffrey.melody;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;

import io.jeffrey.melody.data.NoteStruct;
import io.jeffrey.melody.data.Pitch;
import io.jeffrey.melody.data.Pitch.Step;
import io.jeffrey.melody.imaging.Measure;
import io.jeffrey.melody.imaging.UnicodeNotes;

public class MeasureWalk {

	private final BufferedImage image;
	private final Graphics graphics;
	private UnicodeNotes notes;

	private final int width;
	private final int height;
	private final int durationStep = 12;
	private int measureId = 0;

	private int x;

	private class Staff {
		private final int y;

		private Staff(int y) {
			this.y = y;
		}
	}

	private HashMap<Integer, Staff> staffs;

	public MeasureWalk() throws Exception {
		this.width = 1024;
		this.height = 1024;
		this.image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		this.graphics = image.getGraphics();
		System.out.println("Start notes");
		this.notes = new UnicodeNotes(new Font("Bravura", Font.PLAIN, 48));
		this.staffs = new HashMap<Integer, MeasureWalk.Staff>();
		System.out.println("here");
		staffs.put(1, new Staff(512));
		staffs.put(2, new Staff(700));
	}

	private int y(Step step, int octave, int staff) {
		int offset = (step.asInteger - Step.C.asInteger + (octave - 4) * 7)
				* notes.halfNoteHeight;

		Staff S = staffs.get(staff);

		return S.y - offset;
	}

	private int y(Pitch pitch, int staff) {
		return y(pitch.step, pitch.octave, staff);
	}

	private Measure current;

	public void begin() {
		this.current = new Measure();
		this.last = -1;
		x = notes.noteWidth;
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, width, height);

		graphics.setColor(Color.BLACK);
		int[] bars = new int[] { y(Step.E, 4, 1), y(Step.G, 4, 1),
				y(Step.B, 4, 1), y(Step.D, 5, 1), y(Step.F, 5, 1),
				y(Step.A, 3, 2), y(Step.F, 3, 2), y(Step.D, 3, 2),
				y(Step.B, 2, 2), y(Step.G, 2, 2) };
		for (int y : bars) {
			graphics.drawLine(0, y, width, y);
		}
	}

	int last = -1;

	public void backup(int dX) {
		x -= dX * durationStep;
	}

	public void note(NoteStruct note) {
		if (last > 0 && ! note.chord) {
			x += last * durationStep;
		}
		current.addNote(note, x, y(note.pitch, note.staff));
		last = note.duration;
	}

	public void end() {
		current.draw(graphics, notes);
		try {
			ImageIO.write(image, "png", new File(
					"C:\\Users\\jeff_000\\Desktop\\measure.dump-" + measureId
							+ ".png"));
		} catch (Exception err) {
			err.printStackTrace();
		}
		if (measureId > 5) {
			throw new RuntimeException("err");
		}
		measureId++;
	}
}
