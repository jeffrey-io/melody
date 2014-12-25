package io.jeffrey.melody;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Import {

	private static Document parse(byte[] raw) throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(new ByteArrayInputStream(raw));
	}

	public static Document isolateMusicXML(File file) throws Exception {
		ZipFile zf = new ZipFile(file);
		try {
			Enumeration<? extends ZipEntry> entries = zf.entries();
			HashMap<String, byte[]> broken = new HashMap<String, byte[]>();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				InputStream entryIn = zf.getInputStream(entry);
				try {
					broken.put(entry.getName(), IOUtils.toByteArray(entryIn));
				} finally {
					entryIn.close();
				}
			}
			if (!broken.containsKey("META-INF/container.xml")) {
				throw new Exception("META-INF/container.xml not found");
			}

			Document doc = parse(broken.get("META-INF/container.xml"));
			NodeList list = doc.getElementsByTagName("rootfile");
			// first one is the music xml
			String musicXmlPath = list.item(0).getAttributes()
					.getNamedItem("full-path").getNodeValue();

			if (!broken.containsKey(musicXmlPath)) {
				throw new Exception(musicXmlPath + " not found");
			}

			return parse(broken.get(musicXmlPath));
		} finally {
			zf.close();
		}
	}

}
