package com.zh.coherence.viewer.tools.report.jmx.chamomile;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class QuadraticManager extends DefaultHandler {
    public static final String FILE_PATH = "quadratic.xml";

    public static final String TAG_ARG = "arg";
    public static final String TAG_ITEM = "item";
    public static final String TAG_DATA_PROVIDER = "data-provider";
    public static final String TAG_LABEL = "label";

    private Map<String, List<QuadraticReportItem>> map = new HashMap<String, List<QuadraticReportItem>>();

    private QuadraticReportItem activeItem = null;
    private Object activeElement = null;

    public QuadraticManager() {
        //load config file
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            URL uri = getClass().getResource(FILE_PATH);
            if (uri == null) {
                uri = ClassLoader.getSystemResource(FILE_PATH);
            }
            InputStream stream = uri.openStream();
            parser.parse(stream, this);
            stream.close();
        } catch (Exception ex) {
            System.err.println("Couldn't read file: '" + FILE_PATH);
            ex.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (TAG_ITEM.equalsIgnoreCase(qName)) {
            String name = attributes.getValue("name");
            String target = attributes.getValue("target");
            activeItem = new QuadraticReportItem(name);
            if (!map.containsKey(target)) {
                map.put(target, new ArrayList<QuadraticReportItem>());
            }
            map.get(target).add(activeItem);
        } else if (TAG_DATA_PROVIDER.equalsIgnoreCase(qName)) {
            activeElement = activeItem.initDataProvider(attributes.getValue("class"));
        } else if (TAG_LABEL.equalsIgnoreCase(qName)) {
            activeElement = activeItem.initLabel(attributes.getValue("class"));
        } else if (TAG_ARG.equalsIgnoreCase(qName)) {
            if (activeElement != null && activeElement instanceof QuadraticTagHandler) {
                ((QuadraticTagHandler) activeElement).startElement(uri, localName, qName, attributes);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (activeElement != null && activeElement instanceof QuadraticTagHandler) {
            ((QuadraticTagHandler) activeElement).characters(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (TAG_ARG.equalsIgnoreCase(qName)) {
            if (activeElement != null && activeElement instanceof QuadraticTagHandler) {
                ((QuadraticTagHandler) activeElement).endElement(uri, localName, qName);
            }
        } else if (TAG_DATA_PROVIDER.equalsIgnoreCase(qName) || TAG_LABEL.equalsIgnoreCase(qName)) {
            activeElement = null;
        }
    }

    public Collection<String> getCategories() {
        return map.keySet();
    }

    public Collection<QuadraticReportItem> getReports(String category) {
        return map.get(category);
    }
}
