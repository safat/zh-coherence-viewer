package com.zh.coherence.viewer.menubar;

import com.zh.coherence.viewer.ResourceManager;
import com.zh.coherence.viewer.tools.CoherenceViewerToolCreator;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 1:51
 */
public class CreateToolAction extends AbstractAction {
    private CoherenceViewerToolCreator creator;

    public CreateToolAction(CoherenceViewerToolCreator creator) {
        this.creator = creator;
        this.putValue(Action.NAME, creator.getToolName());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ResourceManager.getInstance().getApplicationPane().addTool(creator.createToolContainer());
    }
}
