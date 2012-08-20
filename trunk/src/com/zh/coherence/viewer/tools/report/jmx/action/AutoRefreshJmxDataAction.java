package com.zh.coherence.viewer.tools.report.jmx.action;

import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AutoRefreshJmxDataAction extends AbstractAction{

    public AutoRefreshJmxDataAction() {
        putValue(Action.SMALL_ICON, new IconLoader("icons/clock.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
