package com.zh.coherence.viewer.tools.statistic.config.actions;

import com.zh.coherence.viewer.tools.statistic.config.TabConfigListModel;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import org.jdesktop.swingx.JXList;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MoveListItemUpAction extends AbstractAction {

    private TabConfigListModel model;

    private JXList jxList;

    public MoveListItemUpAction(TabConfigListModel model, JXList jxList) {
        this.model = model;
        this.jxList = jxList;

        putValue(Action.NAME, "move up");
        putValue(Action.SMALL_ICON, new IconLoader("icons/up.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int[] selected = model.moveContainersUp(jxList.getSelectedValues());
            if (selected != null && selected.length > 0) {
                jxList.setSelectedIndices(selected);
            }
        } catch (IndexOutOfBoundsException ex) {
            System.err.println("WARN: " + ex.getMessage());
        }
    }
}
