package com.zh.coherence.viewer.pof.xml;

import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 08.03.12
 * Time: 2:24
 */
public class XmlPofContextReader {
    public XmlPofConfig readXmlPofConfig(String path, XmlPofConfig pofConfig) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setFeature("http://xml.org/sax/features/validation", false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            URL uri = getClass().getResource(path);
            if(uri == null){
                uri = ClassLoader.getSystemResource(path);
            }

            InputStream stream = uri.openStream();

            parser.parse(stream, new TagHandler(pofConfig));
            stream.close();
        } catch (Exception ex) {
            System.err.println("file: '" + path + "' was not found");
            ex.printStackTrace();
        }

        return pofConfig;
    }

    public XmlPofConfig readXmlPofConfig(String path) {
        return readXmlPofConfig(path, new XmlPofConfig());
    }
}