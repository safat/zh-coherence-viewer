package com.zh.coherence.viewer.tools.report.jmx.quadratic;

import org.xml.sax.Attributes;

public interface QuadraticTagHandler {
    public void startElement(String uri, String localName, String qName, Attributes attributes);

    public void characters(char[] ch, int start, int length);

    public void endElement(String uri, String localName, String qName);
}
