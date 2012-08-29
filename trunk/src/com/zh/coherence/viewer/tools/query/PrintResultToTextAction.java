package com.zh.coherence.viewer.tools.query;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PrintResultToTextAction extends AbstractAction {
    private JToggleButton[] radioButtons;
    private QueryContext context;

    public PrintResultToTextAction(QueryContext context, JToggleButton[] radioButtons) {
        this.radioButtons = radioButtons;
        this.context = context;

        putValue(Action.SMALL_ICON, new IconLoader("icons/text.png"));
        putValue(Action.SHORT_DESCRIPTION, "Result to text");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(JToggleButton radio : radioButtons){
            radio.setSelected(false);
            context.setCurrentOutputTool(QueryContext.TEXT_VIEW);
        }
    }
}
