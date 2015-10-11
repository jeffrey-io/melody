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
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * The purpose of this file is to extract the xml of the notes from a 'mxl' file which is just
 * XML inside of the zip with a container manifest.
 * 
 * @author jeffrey
 */
public class Import {

  /**
   * isolate a single music xml from the given file
   * @param file the file which we suspect is a zipped music xml
   * @return the parsed XML document
   * @throws Exception things went badly
   */
  public static Document isolateMusicXML(final File file) throws Exception {
    final ZipFile zf = new ZipFile(file);
    try {
      final Enumeration<? extends ZipEntry> entries = zf.entries();
      final HashMap<String, byte[]> broken = new HashMap<String, byte[]>();
      while (entries.hasMoreElements()) {
        final ZipEntry entry = entries.nextElement();
        final InputStream entryIn = zf.getInputStream(entry);
        try {
          broken.put(entry.getName(), IOUtils.toByteArray(entryIn));
        } finally {
          entryIn.close();
        }
      }
      if (!broken.containsKey("META-INF/container.xml")) {
        throw new Exception("META-INF/container.xml not found");
      }

      final Document doc = parse(broken.get("META-INF/container.xml"));
      final NodeList list = doc.getElementsByTagName("rootfile");
      if (list.getLength() != 1) {
        throw new Exception("there are multiple things in teh rootfile... not implemented");
      }
      final String musicXmlPath = list.item(0).getAttributes().getNamedItem("full-path").getNodeValue();
      if (!broken.containsKey(musicXmlPath)) {
        throw new Exception(musicXmlPath + " not found");
      }
      return parse(broken.get(musicXmlPath));
    } finally {
      zf.close();
    }
  }

  /**
   * lame helper to parse an xml file into a Document tree
   */
  private static Document parse(final byte[] raw) throws Exception {
    final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    dBuilder.setErrorHandler(new ErrorHandler() {

      public void error(final SAXParseException e) throws SAXException {
        throw e;
      }

      public void fatalError(final SAXParseException e) throws SAXException {
        throw e;
      }

      public void warning(final SAXParseException e) throws SAXException {
        throw e;
      }
    });
    return dBuilder.parse(new ByteArrayInputStream(raw));
  }

}
