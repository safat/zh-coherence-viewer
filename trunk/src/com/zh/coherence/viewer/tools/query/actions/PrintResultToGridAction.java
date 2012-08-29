package com.zh.coherence.viewer.tools.query.actions;

import com.zh.coherence.viewer.tools.query.QueryContext;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PrintResultToGridAction extends AbstractAction{
    private JToggleButton[] radioButtons;
    private QueryContext context;


    public PrintResultToGridAction(QueryContext context, JToggleButton[] radioButtons) {
        this.context = context;
        this.radioButtons = radioButtons;

        putValue(Action.SMALL_ICON, new IconLoader("icons/grid.png"));
        putValue(Action.SHORT_DESCRIPTION, "Result to grid");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(JToggleButton radio : radioButtons){
            radio.setSelected(false);
            context.setCurrentOutputTool(QueryContext.TABLE_VIEW);
        }
    }
}
