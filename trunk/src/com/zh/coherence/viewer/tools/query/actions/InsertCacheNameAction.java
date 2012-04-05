package com.zh.coherence.viewer.tools.query.actions;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.query.QueryTool;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 06.04.12
 * Time: 0:34
 */
public class InsertCacheNameAction extends AbstractAction {
    private QueryTool queryTool;

    public InsertCacheNameAction(QueryTool queryTool) {
        this.queryTool = queryTool;

        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.DATABASE_LINK));
        setEnabled(JMXManager.getInstance().isEnabled());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPopupMenu menu = new JPopupMenu();
        Set<String> names = JMXManager.getInstance().getCacheNamesList();

        for(String name : names){
            menu.add(new InsertTextAction(name, queryTool));
        }

        JComponent component = (JComponent) e.getSource();
        menu.show(component, 0, component.getHeight());
    }

    private class InsertTextAction extends AbstractAction{
        private String name;
        private QueryTool queryTool;

        private InsertTextAction(String name, QueryTool queryTool) {
            this.name = name;
            this.queryTool = queryTool;

            putValue(Action.NAME, name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int caret = queryTool.getEditor().getCaretPosition();
            String text = queryTool.getEditor().getText();
            String leftSymbol = text.substring(caret-1, caret);
            boolean ignoreComa = leftSymbol.equals("'") || leftSymbol.equals("\"");
            if(!ignoreComa){
                name = "'" + name + "'";
            }
            queryTool.getEditor().insert(name, caret);
        }
    }
}
