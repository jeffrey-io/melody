package io.jeffrey.melody;

import java.io.File;

import org.w3c.dom.Document;

public class Scratch {

  public static void main(final String[] args) throws Exception {

    if (args.length == 0) {
      System.err.println("need file argument");
      return;
    }
    final File file = new File(args[0]);
    if (!file.exists()) {
      System.err.println("file must exist");
      return;
    }

    final Document music = Import.isolateMusicXML(file);
    final Walker walker = new Walker();
    walker.walk(music);
  }
}
