package com.zh.coherence.viewer.menubar;

import com.zh.coherence.viewer.ResourceManager;
import com.zh.coherence.viewer.tools.ToolFounder;
import com.zh.coherence.viewer.utils.icons.IconHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CreateToolAction extends AbstractAction {
    private ToolFounder founder;

    public CreateToolAction(ToolFounder founder) {
        this.founder = founder;
        this.putValue(Action.NAME, founder.getDescription().getName());
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(founder.getDescription().getIcon()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ResourceManager.getInstance().getApplicationPane().addTool(
                founder.newTool(), founder.getDescription().getName());
    }

    public ToolFounder getFounder() {
        return founder;
    }
}
