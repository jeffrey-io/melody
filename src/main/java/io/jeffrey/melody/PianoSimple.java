package io.jeffrey.melody;

import java.io.File;
import java.util.ArrayList;

import org.w3c.dom.Document;

import io.jeffrey.melody.data.NoteStruct;
import io.jeffrey.melody.data.Pitch;

public class PianoSimple {
  
  
  public static class PianoNote {
    final Pitch pitch;
    final int start;
    final int length;
    
    public PianoNote(int start, int length, final Pitch pitch) {
      this.start = start;
      this.length = length;
      this.pitch = pitch;
    }
  }

  public static class PianoMeasure {
    final ArrayList<PianoNote> notes;
    
    public PianoMeasure(ArrayList<PianoNote> notes) {
      this.notes = new ArrayList<PianoSimple.PianoNote>(notes);
    }
  }

  public static void main(final String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println(" usage: TOOL file.mxl");
      System.exit(1);
      return;
    }
    final File file = new File(args[0]);
    final Document doc = Import.isolateMusicXML(file);

    final ArrayList<PianoMeasure> score = new ArrayList<PianoSimple.PianoMeasure>();
    new NormalizedPianoWalker(doc) {
      
      private int time = 0;
      private ArrayList<PianoNote> notes;
      
      @Override
      public void unknown(String name) {
      }
      
      @Override
      public void startMeasure() {
        time = 0;
        notes = new ArrayList<PianoSimple.PianoNote>();
      }
      
      @Override
      public void note(NoteStruct note) {
        Pitch pitch = note.pitch;
        int length = note.duration;
        if (pitch != null) {
          notes.add(new PianoNote(time, length, pitch));
        }
        time += length;
      }
      
      @Override
      public void endMeasure() {
        score.add(new PianoMeasure(notes));
      }
      
      @Override
      public void backup(int duration) {
        time -= duration;
      }
    };
    
    
  }
}
