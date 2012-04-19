package com.zh.coherence.viewer.tools.query.actions;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 19.04.12
 * Time: 23:09
 */
public class EventLogHyperlinkListener implements HyperlinkListener {
    public EventLogHyperlinkListener() {
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
            Element element = e.getSourceElement();
            try {
                ((HTMLDocument)element.getParentElement().getDocument()).setInnerHTML(
                        element.getParentElement(), e.getDescription());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
