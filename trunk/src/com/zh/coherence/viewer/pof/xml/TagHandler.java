package com.zh.coherence.viewer.pof.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 08.03.12
 * Time: 3:16
 */
public class TagHandler extends DefaultHandler {
    private XmlPofConfig pofConfig;
    private String currentText;
    private XmlUserType userType;
    private boolean isSerializer;

    public TagHandler(XmlPofConfig pofConfig) {
        this.pofConfig = pofConfig;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if("user-type".equalsIgnoreCase(qName)){
            userType = new XmlUserType();
        }else if("serializer".equalsIgnoreCase(qName)){
            isSerializer = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentText = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("include")){
            XmlPofContextReader reader = new XmlPofContextReader();
            reader.readXmlPofConfig(currentText, pofConfig);
        }else if("type-id".equalsIgnoreCase(qName)){
            userType.type = Integer.parseInt(currentText);
        }else if("class-name".equalsIgnoreCase(qName)){
            if(isSerializer){
                userType.serializer = currentText;
            }else{
                userType.clazz = currentText;
            }
        }else if("serializer".equalsIgnoreCase(qName)){
            isSerializer = false;
        }else if("user-type".equalsIgnoreCase(qName)){
            pofConfig.add(userType);
        }
    }
}
