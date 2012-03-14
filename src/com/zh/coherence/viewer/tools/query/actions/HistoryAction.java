package com.zh.coherence.viewer.tools.query.actions;

import com.zh.coherence.viewer.tools.query.QueryTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 14.03.12
 * Time: 23:48
 */
public class HistoryAction extends AbstractAction{
    private QueryTool queryTool;

    public HistoryAction(QueryTool queryTool) {
        this.queryTool = queryTool;

        putValue(Action.NAME, "History");
        putValue(Action.SMALL_ICON, new ImageIcon("icons/history.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPopupMenu menu = new JPopupMenu();
        for(String str : queryTool.getHistory()){
            menu.add(new LoadFromHistoryAction(str));
        }

        Container cont = (Container) e.getSource();
        menu.show(cont, 0, cont.getHeight());
    }

    private class LoadFromHistoryAction extends AbstractAction{
        private String value;

        private LoadFromHistoryAction(String value) {
            this.value = value;

            putValue(Action.NAME, value);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            queryTool.getEditor().setText(value);
        }
    }
}
