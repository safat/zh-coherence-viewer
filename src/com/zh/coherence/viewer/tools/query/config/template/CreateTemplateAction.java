package com.zh.coherence.viewer.tools.query.config.template;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CreateTemplateAction extends AbstractAction {

    private TemplateListModel listModel;

    public CreateTemplateAction(TemplateListModel listModel) {
        this.listModel = listModel;

        putValue(Action.SMALL_ICON, new IconLoader("icons/plus.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
