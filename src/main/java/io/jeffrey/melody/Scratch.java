package io.jeffrey.melody;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class Scratch {

	private static void show(Document document) throws Exception {

		// Use a Transformer for output
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();

		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(System.out);
		transformer.transform(source, result);
	}

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.err.println("need file argument");
			return;
		}
		File file = new File(args[0]);
		if (!file.exists()) {
			System.err.println("file must exist");
			return;
		}

		Document music = Import.isolateMusicXML(file);

		Walker walker = new Walker();
		walker.walk(music);
		// show(music);
	}
}
