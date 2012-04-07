package com.zh.coherence.viewer.eventlog;

import org.jdesktop.swingx.JXEditorPane;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 07.04.12
 * Time: 16:25
 */
public class EventLogPane extends JPanel {
    private EventLogRenderer renderer;

    private final int LOG_SIZE = 200;

    private List messages = new LinkedList();

    private JXEditorPane text = new JXEditorPane();

    private JScrollPane scrollPane;

    private String header = null;
    private StringBuilder body = new StringBuilder();
    private String footer = "</body></html>";

    public EventLogPane(EventLogRenderer renderer) {
        super(new BorderLayout());
        if(renderer == null){
            throw new IllegalArgumentException("renderer");
        }
        this.renderer = renderer;

        text.setEditable(false);
        text.setEditorKit(new HTMLEditorKit());
        scrollPane = new JScrollPane(text);
        scrollPane.setAutoscrolls(true);

        add(scrollPane, BorderLayout.CENTER);

        header = "<html><head>" + renderer.getCssStyle() + "</head><body>";
    }

    public void addMessage(Object message){
        messages.add(message);

        if(messages.size() > LOG_SIZE){
            for(int i = 0; i < 5; i++){
                messages.remove(0);
            }
            renderAll();
        }else {
            render(message);
        }
        updateText();
    }

    private void render(Object msg){
        body.append(renderer.renderMessage(msg));
    }

    private void renderAll(){
        body = new StringBuilder();
        for(Object msg : messages){
            render(msg);
        }
    }

    private void updateText(){
        text.setText(header + body + footer);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            }
        });
    }
}
