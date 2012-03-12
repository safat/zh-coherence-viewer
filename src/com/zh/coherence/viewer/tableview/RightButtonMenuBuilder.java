package com.zh.coherence.viewer.tableview;

import com.zh.coherence.viewer.hexPanel.ShowHexViewerAction;
import com.zh.coherence.viewer.pof.ValueContainer;
import com.zh.coherence.viewer.tableview.actions.ShowObjectExplorer;
import com.zh.coherence.viewer.tableview.actions.ShowObjectInText;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 29.02.12
 * Time: 20:31
 */
public class RightButtonMenuBuilder {
    public JPopupMenu buildMenu(Object value){
        JPopupMenu menu = new JPopupMenu();
        if(value != null){
            menu.add(new ShowObjectInText(value));
            menu.add(new ShowObjectExplorer(value));

            if(value instanceof ValueContainer){
                menu.addSeparator();
                ValueContainer vc = (ValueContainer) value;

                menu.add(new JMenuItem(new ShowHexViewerAction(vc.getBinary())));
            }
        }
        return menu;
    }
}
